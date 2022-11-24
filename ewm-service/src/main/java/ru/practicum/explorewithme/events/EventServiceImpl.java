package ru.practicum.explorewithme.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.client.MyPageRequest;
import ru.practicum.explorewithme.events.dto.*;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.request.RequestDto;
import ru.practicum.explorewithme.request.RequestRepository;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventClient eventClient;
    private final MapperEvents mapperEvents;

    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, Long userId) throws ParseException {
        Event event = eventRepository.save(mapperEvents.toEvent(newEventDto,
                userId, newEventDto.getLocation()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        Category category = categoryRepository.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundException("Такая категория не существует."));
        log.debug("Добавлено новое событие {}  ", event);
        return mapperEvents.toEventFullDto(event,
                newEventDto.getLocation(),
                new UserDtoShort(user.getId(), user.getName()),
                new CategoryDto(category.getId(), category.getName()));
    }

    @Override
    public EventFullDto getById(long userId, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (event.getInitiator() != userId) {
            throw new ValidationException("Не достаточно прав для просмотра.");
        }
        log.debug("Получено событие {}  ", event);
        return getEventFullDto(userId, event);
    }

    @Override
    public EventFullDto cancel(long userId, long eventId) {
        validateRequest(userId, eventId);
        Event eventOld = eventRepository.findById(eventId).get();
        if (!eventOld.getState().equals(StatusEvent.PENDING)) {
            throw new NotFoundException("Событие состоится в любом случае.");
        }
        eventOld.setState(StatusEvent.CANCELED);
        Event event = eventRepository.save(eventOld);
        log.debug("Отменено событие {}  ", event);
        return getEventFullDto(userId, event);
    }

    @Override
    public List<EventFullDto> getAllEvents(long userId, Integer from, Integer size) {
        MyPageRequest myPageRequest = new MyPageRequest(from, size, Sort.unsorted());
        List<Event> events = eventRepository.findAllByInitiator(userId, myPageRequest);
        List<EventFullDto> eventsFullDto = new ArrayList<>();
        for (Event event : events) {
            eventsFullDto.add(getEventFullDto(event.getInitiator(), event));
        }
        log.debug("Получены от пользователя {} все собития {}", userId, eventsFullDto);
        return eventsFullDto;
    }

    @Override
    public EventFullDto patch(Long userId, UpdateEventRequest updateEventRequest) throws ParseException {
        validateRequest(userId, updateEventRequest.getEventId());
        Event eventOld = eventRepository.findById(updateEventRequest.getEventId()).get();
        if (eventOld.getState().equals(StatusEvent.PUBLISHED)) {
            throw new NotFoundException("Изменить событие уже не удастся.");
        }
        if (mapperEvents.dateFormat.parse(updateEventRequest.getEventDate()).getTime()
                - new Date().getTime() < 7200000) {
            throw new ValidationException("Событие начнется слишком поздно.");
        }
        Event update = mapperEvents.toUpdateEvent(eventOld, updateEventRequest);
        log.debug("Изменено событие {}  ", update);
        return getEventFullDto(userId, update);
    }

    @Override
    public RequestDto addRequestEvent(Long userId, Long eventId) {
        validate(userId, eventId);
        Event eventOld = eventRepository.findById(eventId).get();
        Request request = requestRepository.save(new Request(null, new Date(), eventId,
                userId, eventOld.getParticipantLimit() == 0 ?
                StatusEvent.PUBLISHED : StatusEvent.PENDING));
        log.debug("Добавлен запрос на участие в событии {}  ", request);
        if (eventOld.getParticipantLimit() == 0) {
            eventOld.setConfirmedRequests(eventOld.getConfirmedRequests() + 1);
            eventRepository.save(eventOld);
        }
        return toRequestDto(request);
    }

    @Override
    public RequestDto cancelParticipation(Long requestId, Long userId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Такой запрос не существует."));
        if (request.getStatus().equals(StatusEvent.PUBLISHED)) {
            Event event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
            if (event.getParticipantLimit() != 0) {
                event.setConfirmedRequests(event.getConfirmedRequests() - 1);
                eventRepository.save(event);
            }
        }
        request.setStatus(StatusEvent.CANCELED);
        Request newRequest = requestRepository.save(request);
        log.debug("Отменен запрос на участие в событии {}  ", newRequest);
        return toRequestDto(newRequest);
    }

    @Override
    public Collection<RequestDto> getRequestUser(Long userId) {
        List<Request> requests = requestRepository.findAllByRequester(userId);
        log.debug("Получены все запросы на участие от пользователя {} в событиях {}  ", userId, requests);
        return requests.stream().map(this::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto publish(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        valid(event);
        event.setState(StatusEvent.PUBLISHED);
        event.setPublishedOn(new Date());
        Event newEvent = eventRepository.save(event);
        log.debug("Опубликовано событие {}  ", newEvent);
        return getEventFullDto(newEvent.getInitiator(), event);
    }

    @Override
    public EventFullDto update(Long eventId, NewEventDto newEventDto) throws ParseException {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        Event event = eventRepository.save(toEvent(oldEvent, newEventDto));
        log.debug("Обновлено событие {}  ", event);
        return getEventFullDto(event.getInitiator(), event);
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (event.getState().equals(StatusEvent.PUBLISHED)) {
            throw new ValidationException("Событие опубликовано.");
        }
        event.setState(StatusEvent.CANCELED);
        Event newEvent = eventRepository.save(event);
        log.debug("Отклонена публикация события {}  ", newEvent);
        return getEventFullDto(newEvent.getInitiator(), event);
    }

    public List<EventFullDto> getSearchEvents(Collection<Long> users, List<StatusEvent> states,
                                              Collection<Integer> category, String start, String end,
                                              Integer from, Integer size) throws ParseException {
        MyPageRequest myPageRequest = new MyPageRequest(from, size, Sort.by("event_Date"));
        Collection<String> strings = states.stream().map(Enum::toString).collect(Collectors.toList());
        List<Event> events = eventRepository
                .findAllByInitiatorInAndStateInAndCategoryInAndEventDateAfterAndEventDateBefore(
                        users, strings, category, mapperEvents.dateFormat.parse(start),
                        mapperEvents.dateFormat.parse(end), myPageRequest);
        List<EventFullDto> eventsFullDto = new ArrayList<>();
        for (Event event : events) {
            eventsFullDto.add(getEventFullDto(event.getInitiator(), event));
        }
        log.debug("Получен список событий {}  ", eventsFullDto);
        return eventsFullDto;
    }

    @Override
    public List<RequestDto> getRequestUserById(Long userId, Long eventId) {
        validateRequest(userId, eventId);
        List<Request> request = requestRepository.findAllByEventId(eventId);
        log.debug("Получены запросы на участие {} в событии {} пользователя {}", request, eventId, userId);
        return request.stream().map(this::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmRequestUser(Long userId, Long eventId, Long reqId) {
        boolean state = validRequest(eventId);
        Request request = requestRepository.findByIdAndEventId(reqId, eventId);
        request.setStatus(state ? StatusEvent.CONFIRMED : StatusEvent.REJECTED);
        Request newRequest = requestRepository.save(request);
        if (state) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                List<Request> requests = requestRepository.findAllByEventId(eventId);
                for (Request req : requests) {
                    req.setStatus(StatusEvent.REJECTED);
                    requestRepository.save(req);
                }
            }
        }
        log.debug("Изменен статус запроса на участие в событии {} ", newRequest);
        return toRequestDto(newRequest);
    }

    @Override
    public EventFullDto getEventPublic(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, StatusEvent.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Такое событие не существует.");
        }
        eventClient.addHit(new HitDto(null, "ewm-service",
                request.getRequestURI(), request.getRemoteAddr(), new Date()));
        List<Event> events = new ArrayList<>();
        events.add(event);
        List<Event> eventWithViews = setViews(events);
        Event newEvent = eventWithViews.stream().findAny().get();
        log.debug("Получено событие {}  ", newEvent);
        return getEventFullDto(newEvent.getInitiator(), newEvent);
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid,
                                         String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                         String sort, Integer from, Integer size, HttpServletRequest request) throws ParseException {
        MyPageRequest myPageRequest;
        if (sort.equals("VIEWS")) {
            myPageRequest = new MyPageRequest(from, size, Sort.by("views").ascending());
        } else {
            myPageRequest = new MyPageRequest(from, size, Sort.by("event_Date").ascending());
        }
        List<Event> events;
        if (rangeStart.length() == 0) {
            rangeStart = mapperEvents.dateFormat.format(new Date());
            events = eventRepository.findAllByParam(StatusEvent.PUBLISHED.toString(), text, categories,
                    mapperEvents.dateFormat.parse(rangeStart), paid, myPageRequest);
            System.out.println(events);
        } else {
            events = eventRepository.findAllByParam(StatusEvent.PUBLISHED.toString(), text, categories,
                    mapperEvents.dateFormat.parse(rangeStart), mapperEvents.dateFormat.parse(rangeEnd), paid, myPageRequest);
        }
        eventClient.addHit(new HitDto(null, "ewm-service",
                request.getRequestURI(), request.getRemoteAddr(), new Date()));
        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        List<Event> eventsWithViews = setViews(events);
        log.debug("Получен список событий {}  ", eventsWithViews);
        return toEventShort(eventsWithViews);
    }

    private List<Event> setViews(List<Event> events) {
        String[] uris = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            uris[i] = "/events/" + events.get(i).getId();
        }
        ResponseEntity<Object> statsDto = eventClient.getHits("1900-11-23 00:48:09",
                "2100-11-23 00:48:09", uris, false);
        List<LinkedHashMap<Object, Object>> linkedHashMap = (List<LinkedHashMap<Object, Object>>) statsDto.getBody();
        if (linkedHashMap != null) {
            if (linkedHashMap.size() > 0) {
                for (int i = 0; i < events.size(); i++) {
                    Integer hits = (Integer) linkedHashMap.get(0).get("hits");
                    String uri = (String) linkedHashMap.get(0).get("uri");
                    String[] id = uri.split("/");
                    for (Event e : events) {
                        if (e.getId().equals(Long.parseLong(id[2]))) {
                            int index = events.indexOf(e);
                            e.setViews(hits);
                            events.set(index, e);
                        }
                    }
                    events.get(i).setViews(hits);
                }
            }
        }

        return events;
    }

    private EventFullDto getEventFullDto(long userId, Event event) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        Category category = categoryRepository.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundException("Такая категория не существует."));
        return mapperEvents.toEventFullDto(event,
                new LocationDto(event.getLat(), event.getLon()),
                new UserDtoShort(user.getId(), user.getName()),
                new CategoryDto(category.getId(), category.getName()));
    }

    private List<EventShortDto> toEventShort(List<Event> events) {
        return getEventsShortDto(events, categoryRepository, userRepository, mapperEvents);
    }

    public static List<EventShortDto> getEventsShortDto(List<Event> events, CategoryRepository categoryRepository,
                                                        UserRepository userRepository, MapperEvents mapperEvents) {
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        for (Event event : events) {
            Category category = categoryRepository.findById(event.getCategory())
                    .orElseThrow(() -> new NotFoundException("Такая категория не существует."));
            User user = userRepository.findById(event.getInitiator())
                    .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
            eventsShortDto.add(mapperEvents.toEventShortDto(event, new UserDtoShort(user.getId(), user.getName()),
                    new CategoryDto(category.getId(), category.getName())));
        }
        return eventsShortDto;
    }

    @Override
    public RequestDto rejectRequestUser(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository.findByIdAndEventId(reqId, eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (event.getInitiator() != userId) {
            throw new ValidationException("Вы не являетесь организатором и не можете отклонить заявку.");
        }
        request.setStatus(StatusEvent.REJECTED);
        Request newRequest = requestRepository.save(request);
        log.debug("Отклонен запрос на участие в событии {}", newRequest);
        return toRequestDto(newRequest);
    }

    private void validate(Long userId, Long eventId) {
        Event eventOld = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (eventOld.getInitiator() == userId) {
            throw new ValidationException("Вы не можете подать запрос на участие в своем собитии.");
        }
        if (!eventOld.getState().equals(StatusEvent.PUBLISHED)) {
            throw new ValidationException("Вы не можете участвовать в неопубликованном собитии.");
        }
        if (eventOld.getConfirmedRequests() == eventOld.getParticipantLimit() &&
                eventOld.getConfirmedRequests() != 0) {
            throw new ValidationException("Достигнуто максимальное количесвто участников в собитии.");
        }
    }

    private void valid(Event event) {
        if (!event.getState().equals(StatusEvent.PENDING)) {
            throw new NotFoundException("Событие отменено.");
        }
        if (event.getEventDate().getTime() - new Date().getTime() < 7200000) {
            throw new ValidationException("Событие начнется слишком поздно.");
        }
    }

    private boolean validRequest(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new IllegalArgumentException("Подтверждение заявки не требуется.");
        }
        return event.getConfirmedRequests() != event.getParticipantLimit();
    }

    private void validateRequest(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (event.getInitiator() != userId) {
            throw new ValidationException("Вы не являетесь организатором.");
        }
    }

    private Event toEvent(Event oldEvent, NewEventDto newEventDto) throws ParseException {
        return mapperEvents.updateEvent(oldEvent, newEventDto);
    }

    private RequestDto toRequestDto(Request request) {
        return new RequestDto(request.getId(), request.getCreated(),
                request.getEventId(), request.getRequester(), request.getStatus());
    }
}
