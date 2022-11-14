package ru.practicum.explorewithme.events;

import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventUpdate;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.request.RequestDto;

import javax.validation.constraints.Positive;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(NewEventDto newEventDto, Long userId) throws ParseException;

    EventFullDto getById(long userId, long eventId);

    EventFullDto cancel(long userId, long eventId);

    List<EventFullDto> getAllEvents(@Positive long userId, Integer from, Integer size);

    EventFullDto patch(long userId, EventUpdate eventUpdate) throws ParseException;

    RequestDto addRequestEvent(Long userId, Long eventId);

    RequestDto cancelParticipation(Long requestId, Long userId);

    Collection<RequestDto> getRequestUser(Long userId);

    EventFullDto publish(Long eventId);

    EventFullDto update(Long eventId, NewEventDto newEventDto) throws ParseException;

    EventFullDto rejectEvent(Long eventId);
}
