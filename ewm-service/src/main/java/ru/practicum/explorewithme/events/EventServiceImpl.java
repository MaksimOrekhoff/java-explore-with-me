package ru.practicum.explorewithme.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.client.MyPageRequest;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventUpdate;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.locations.Location;
import ru.practicum.explorewithme.locations.LocationDto;
import ru.practicum.explorewithme.locations.LocationRepository;
import ru.practicum.explorewithme.request.Request;
import ru.practicum.explorewithme.request.RequestDto;
import ru.practicum.explorewithme.request.RequestRepository;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final MapperEvents mapperEvents;

    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, Long userId) throws ParseException {
        Location location = locationRepository.save(new Location(null,
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon()));
        Event event = eventRepository.save(mapperEvents.toEvent(newEventDto,
                userId, location.getId()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        Category category = categoryRepository.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundException("Такая категория не существует."));
        log.debug("Добавлено новое событие {}  ", event);
        return mapperEvents.toEventFullDto(event,
                new LocationDto(location.getLat(), location.getLon()),
                new UserDtoShort(user.getId(), user.getName()),
                new CategoryDto(category.getId(), category.getName()));
    }

    @Override
    public EventFullDto getById(long userId, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (event.getInitiator() != userId) {
            throw new NotFoundException("Не достаточно прав для просмотра.");
        }
        log.debug("Получено событие {}  ", event);
        return getEventFullDto(userId, event);
    }

    @Override
    public EventFullDto cancel(long userId, long eventId) {
        Event eventOld = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (eventOld.getInitiator() != userId) {
            throw new NotFoundException("Не достаточно прав для изменения статуса.");
        }
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
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        for (Event event : events) {
            eventFullDtos.add(getEventFullDto(event.getInitiator(), event));
        }
        log.debug("Получены все собития");
        return eventFullDtos;
    }

    @Override
    public EventFullDto patch(long userId, EventUpdate eventUpdate) throws ParseException {
        Event eventOld = eventRepository.findById(eventUpdate.getEventId())
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (eventOld.getInitiator() != userId) {
            throw new NotFoundException("Не достаточно прав для изменения статуса.");
        }
        if (eventOld.getState().equals(StatusEvent.PUBLISHED)) {
            throw new NotFoundException("Изменить событие уже не удастся.");
        }
        Event update = mapperEvents.toUpdateEvent(eventOld, eventUpdate);
        log.debug("Изменено событие {}  ", update);
        return getEventFullDto(userId, update);
    }

    @Override
    public RequestDto addRequestEvent(Long userId, Long eventId) {
        validate(userId, eventId);
        Event eventOld = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));

        Request request = requestRepository.save(new Request(null, new Date(), eventId,
                userId, eventOld.getParticipantLimit() == 0 ?
                StatusEvent.PUBLISHED : StatusEvent.PENDING));
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
        return toRequestDto(newRequest);
    }

    @Override
    public Collection<RequestDto> getRequestUser(Long userId) {
        List<Request> requests = requestRepository.findAllByRequester(userId);
        return requests.stream().map(this::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto publish(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        valid(event);
        event.setState(StatusEvent.PUBLISHED);
        Event newEvent = eventRepository.save(event);
        return getEventFullDto(newEvent.getInitiator(), event);
    }

    @Override
    public EventFullDto update(Long eventId, NewEventDto newEventDto) throws ParseException {
        Event oldEvent = eventRepository.findById(eventId).get();
        Event event = eventRepository.save(toEvent(oldEvent, newEventDto));
        return getEventFullDto(event.getInitiator(), event);
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (event.getState().equals(StatusEvent.PUBLISHED)) {
            throw new NotFoundException("Событие опубликовано.");
        }
        event.setState(StatusEvent.CANCELED);
        Event newEvent = eventRepository.save(event);
        return getEventFullDto(newEvent.getInitiator(), event);
    }

    private Event toEvent(Event oldEvent, NewEventDto newEventDto) throws ParseException {
        oldEvent.setAnnotation(newEventDto.getAnnotation());
        oldEvent.setCategory(newEventDto.getCategory());
        oldEvent.setDescription(newEventDto.getDescription());
        oldEvent.setEventDate(mapperEvents.dateFormat.parse(newEventDto.getEventDate()));
        //locationId
        oldEvent.setPaid(newEventDto.getPaid());
        oldEvent.setParticipantLimit(newEventDto.getParticipantLimit());
        oldEvent.setRequestModeration(newEventDto.getRequestModeration());
        oldEvent.setTitle(newEventDto.getTitle());
        return oldEvent;
    }

    private void valid(Event event) {
        if (!event.getState().equals(StatusEvent.PENDING)) {
            throw new NotFoundException("Событие отменено.");
        }
        if (event.getEventDate().getTime() - new Date().getTime() < 3600000) {
            throw new NotFoundException("Событие начнется слишком поздно.");
        }
    }


    private EventFullDto getEventFullDto(long userId, Event event) {
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(event.getCategory()).get();
        Location location = locationRepository.findById(event.getLocation()).get();
        return mapperEvents.toEventFullDto(event,
                new LocationDto(location.getLat(), location.getLon()),
                new UserDtoShort(user.getId(), user.getName()),
                new CategoryDto(category.getId(), category.getName()));
    }

    private void validate(Long userId, Long eventId) {
        Event eventOld = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (eventOld.getInitiator() == userId) {
            throw new NotFoundException("Вы не можете подать запрос на участие в своем собитии.");
        }
        if (!eventOld.getState().equals(StatusEvent.PUBLISHED)) {
            throw new NotFoundException("Вы не можете участвовать в неопубликованном собитии.");
        }
        if (eventOld.getConfirmedRequests() == eventOld.getParticipantLimit() &&
                eventOld.getConfirmedRequests() != 0) {
            throw new NotFoundException("Достигнуто максимальное количесвто участников в собитии.");
        }
    }

    private RequestDto toRequestDto(Request request) {
        return new RequestDto(request.getId(), request.getCreated(),
                request.getEventId(), request.getRequester(), request.getStatus());
    }
}
