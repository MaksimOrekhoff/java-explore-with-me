package ru.practicum.explorewithme.events;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventUpdate;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.locations.LocationDto;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MapperEvents {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Event toEvent(NewEventDto newEventDto, long userId, long locationId) throws ParseException {
        return new Event(null,
                newEventDto.getAnnotation(),
                newEventDto.getCategory(),
                0,
                new Date(),
                newEventDto.getDescription(),
                dateFormat.parse(newEventDto.getEventDate()),
                userId,
                locationId,
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(),
                StatusEvent.PENDING,
                newEventDto.getTitle(),
                0);
    }

    public EventFullDto toEventFullDto(Event event, LocationDto locationDto,
                                       UserDtoShort userDtoShort, CategoryDto categoryDto) {
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                categoryDto,
                event.getConfirmedRequests(),
                dateFormat.format(event.getCreatedOn()),
                event.getDescription(),
                dateFormat.format(event.getEventDate()),
                userDtoShort,
                locationDto,
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn() == null ? null : dateFormat.format(event.getPublishedOn()),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews());
    }

    public Event toUpdateEvent(Event eventOld, EventUpdate eventUpdate) throws ParseException {
        eventOld.setAnnotation(eventUpdate.getAnnotation());
        eventOld.setCategory(eventUpdate.getCategory());
        eventOld.setDescription(eventUpdate.getDescription());
        eventOld.setEventDate(dateFormat.parse(eventUpdate.getEventDate()));
        eventOld.setPaid(eventUpdate.getPaid());
        eventOld.setParticipantLimit(eventUpdate.getParticipantLimit());
        eventOld.setTitle(eventUpdate.getTitle());
        return eventOld;
    }
}
