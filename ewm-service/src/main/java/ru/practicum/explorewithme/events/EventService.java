package ru.practicum.explorewithme.events;

import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventShortDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.dto.UpdateEventRequest;
import ru.practicum.explorewithme.request.RequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(NewEventDto newEventDto, Long userId) throws ParseException;

    EventFullDto getById(long userId, long eventId);

    EventFullDto cancel(long userId, long eventId);

    List<EventFullDto> getAllEvents(@Positive long userId, Integer from, Integer size);

    EventFullDto patch(Long userId, UpdateEventRequest updateEventRequest) throws ParseException;

    RequestDto addRequestEvent(Long userId, Long eventId);

    RequestDto cancelParticipation(Long requestId, Long userId);

    Collection<RequestDto> getRequestUser(Long userId);

    EventFullDto publish(Long eventId);

    EventFullDto update(Long eventId, NewEventDto newEventDto) throws ParseException;

    EventFullDto rejectEvent(Long eventId);

    List<EventFullDto> getSearchEvents(Collection<Long> users, List<StatusEvent> states, Collection<Integer> category, String start, String end, Integer from, Integer size) throws ParseException;

    List<RequestDto> getRequestUserById(Long userId, Long eventId);

    RequestDto confirmRequestUser(Long userId, Long eventId, Long reqId);

    RequestDto rejectRequestUser(Long userId, Long eventId, Long reqId);

    EventFullDto getEventPublic(Long eventId, HttpServletRequest request) throws ParseException;

    List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) throws ParseException;


}
