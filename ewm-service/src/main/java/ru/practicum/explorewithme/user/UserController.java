package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.events.EventService;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.dto.UpdateEventRequest;
import ru.practicum.explorewithme.request.RequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

/**
 * TODO graduation project
 */

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventFullDto> allEventsUser(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                            @PathVariable @NotBlank long userId) {
        log.info("Получен Get-запрос на получение всех событий пользователя c id: {}", userId);
        return eventService.getAllEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventCreatingUser(@PathVariable @NotBlank long userId,
                                             @PathVariable @NotBlank long eventId) {
        log.info("Получен Get-запрос на получение пользователем c id: {} события : {}", userId, eventId);
        return eventService.getById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEventUser(@PathVariable @NotBlank long userId,
                                        @PathVariable @NotBlank long eventId) {
        log.info("Получен Patch-запрос на отмену события c id: {} от пользователя c id: {}", eventId, userId);
        return eventService.cancel(userId, eventId);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto patchEventUser(@Validated @RequestBody UpdateEventRequest updateEventRequest,
                                       @PathVariable @NotBlank long userId) throws ParseException {
        log.info("Получен Patch-запрос на изменение события от пользователя c id: {}", userId);
        return eventService.patch(userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto postUserEvent(@Validated @RequestBody NewEventDto newEventDto,
                                      @PathVariable @NotBlank Long userId) throws ParseException {
        log.debug("Получен Post-запрос на добавление нового события {}", newEventDto);
        return eventService.addEvent(newEventDto, userId);
    }

    @PostMapping("/{userId}/requests")
    public RequestDto postRequestUserEvent(@NotBlank @RequestParam(name = "eventId") Long eventId,
                                           @PathVariable @NotBlank Long userId) {
        log.debug("Получен Post-запрос на запрос от пользователя {} на участие в событии {}", userId, eventId);
        return eventService.addRequestEvent(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<RequestDto> getRequestUserEvent(@PathVariable @Positive Long userId) {
        log.debug("Получен Get-запрос от пользователя {} на участие в событиях", userId);
        return eventService.getRequestUser(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelParticipationEventUser(@PathVariable @NotBlank Long requestId,
                                                   @PathVariable @NotBlank Long userId) {
        log.info("Получен Patch-запрос на отмену участия в событии {} от пользователя {}", requestId, userId);
        return eventService.cancelParticipation(requestId, userId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getInfoRequestUser(@PathVariable @NotBlank long userId,
                                               @PathVariable @NotBlank long eventId) {
        log.info("Получен Get-запрос на получение информации о запросах на участие в событии c id: {} от пользователя: {}", eventId, userId);
        return eventService.getRequestUserById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable @NotBlank long userId,
                                     @PathVariable @NotBlank long eventId,
                                     @PathVariable @NotBlank long reqId) {
        log.info("Получен Patch-запрос на подтверждение заявки на участие c id: {} в событии : {} созданного пользователем c id: {}", eventId, reqId, userId);
        return eventService.confirmRequestUser(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable @NotBlank long userId,
                                    @PathVariable @NotBlank long eventId,
                                    @PathVariable @NotBlank long reqId) {
        log.info("Получен Patch-запрос на отклонение заявки на участие c id: {} в событии : {} созданного пользователем c id: {}", eventId, reqId, userId);
        return eventService.rejectRequestUser(userId, eventId, reqId);
    }
}
