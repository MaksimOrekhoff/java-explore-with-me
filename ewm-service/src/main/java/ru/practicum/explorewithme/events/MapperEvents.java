package ru.practicum.explorewithme.events;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.events.dto.*;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MapperEvents {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Event toEvent(NewEventDto newEventDto, long userId, LocationDto locationDto) throws ParseException {
        return new Event(null,
                newEventDto.getAnnotation(),
                newEventDto.getCategory(),
                0,
                new Date(),
                newEventDto.getDescription(),
                dateFormat.parse(newEventDto.getEventDate()),
                userId,
                locationDto.getLat(),
                locationDto.getLon(),
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

    public Event toUpdateEvent(Event eventOld, UpdateEventRequest updateEventRequest) throws ParseException {
        eventOld.setAnnotation(updateEventRequest.getAnnotation());
        eventOld.setCategory(updateEventRequest.getCategory());
        eventOld.setDescription(updateEventRequest.getDescription());
        eventOld.setEventDate(dateFormat.parse(updateEventRequest.getEventDate()));
        eventOld.setPaid(updateEventRequest.getPaid());
        eventOld.setParticipantLimit(updateEventRequest.getParticipantLimit());
        eventOld.setTitle(updateEventRequest.getTitle());
        return eventOld;
    }

    public EventShortDto toEventShortDto(Event event, UserDtoShort userDtoShort, CategoryDto categoryDto) {
        return new EventShortDto(event.getAnnotation(),
                categoryDto,
                event.getConfirmedRequests(),
                dateFormat.format(event.getEventDate()),
                event.getId(),
                userDtoShort,
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public Event updateEvent(Event oldEvent, NewEventDto newEventDto) throws ParseException {
        oldEvent.setAnnotation(newEventDto.getAnnotation() != null ? newEventDto.getAnnotation()
                : oldEvent.getAnnotation());
        oldEvent.setCategory(newEventDto.getCategory() != null ? newEventDto.getCategory()
                : oldEvent.getCategory());
        oldEvent.setDescription(newEventDto.getDescription() != null ? newEventDto.getDescription() : oldEvent.getDescription());
        oldEvent.setEventDate(newEventDto.getEventDate() != null ?
                dateFormat.parse(newEventDto.getEventDate())
                : oldEvent.getEventDate());
        oldEvent.setPaid(newEventDto.getPaid() != null ? newEventDto.getPaid()
                : oldEvent.getPaid());
        oldEvent.setParticipantLimit(newEventDto.getParticipantLimit() != null ? newEventDto.getParticipantLimit()
                : oldEvent.getParticipantLimit());
        oldEvent.setRequestModeration(newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration()
                : oldEvent.getRequestModeration());
        oldEvent.setTitle(newEventDto.getTitle() != null ? newEventDto.getTitle()
                : oldEvent.getTitle());
        if (newEventDto.getLocation() != null) {
            oldEvent.setLon(newEventDto.getLocation().getLon());
            oldEvent.setLat(newEventDto.getLocation().getLat());
        }
        return oldEvent;
    }
}
