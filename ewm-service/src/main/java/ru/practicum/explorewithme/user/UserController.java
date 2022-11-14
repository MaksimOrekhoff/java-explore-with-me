package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.events.EventService;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventUpdate;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.request.RequestDto;
import ru.practicum.explorewithme.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;


/**
 * TODO Sprint add-controllers.
 */

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Object> postUser(@Valid @RequestBody UserDto userDto) {
        log.debug("Получен Post-запрос на добавление пользователя");
        return userClient.addUser(userDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventFullDto> allEventsUser(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                            @PathVariable @Positive long userId) {
        log.info("Получен Get-запрос на получение всех событий пользователя c id: {}", userId);
        return eventService.getAllEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventCreatingUser(@PathVariable @Positive long userId,
                                             @PathVariable @Positive long eventId) {
        log.info("Получен Get-запрос на получение пользователем c id: {} события : {}", userId, eventId);
        return eventService.getById(userId, eventId);
    }


    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEventUser(@PathVariable @Positive long userId,
                                        @PathVariable @Positive long eventId) {
        log.info("Получен Patch-запрос на отмену события c id: {} от пользователя c id: {}", eventId, userId);
        return eventService.cancel(userId, eventId);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto patchEventUser(@Validated @RequestBody EventUpdate eventUpdate,
                                       @PathVariable @Positive long userId) throws ParseException {
        log.info("Получен Patch-запрос на изменение события от пользователя c id: {}", userId);
        return eventService.patch(userId, eventUpdate);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto postUserEvent(@Validated @RequestBody NewEventDto newEventDto,
                                      @PathVariable Long userId) throws ParseException {
        log.debug("Получен Post-запрос на добавление нового события {}", newEventDto);
        return eventService.addEvent(newEventDto, userId);
    }

    @PostMapping("/{userId}/requests")
    public RequestDto postRequestUserEvent(@PositiveOrZero @RequestParam(name = "eventId") Long eventId,
                                           @PathVariable @Positive Long userId) {
        log.debug("Получен Post-запрос на запрос от пользователя {} на участие в событии {}", userId, eventId);
        return eventService.addRequestEvent(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<RequestDto> getRequestUserEvent(@PathVariable @Positive Long userId) {
        log.debug("Получен Get-запрос от пользователя {} на участие в событиях", userId);
        return eventService.getRequestUser(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelParticipationEventUser(@PathVariable @Positive Long requestId,
                                                   @PathVariable @Positive long userId) {
        log.info("Получен Patch-запрос на отмену участия в событии {} от пользователя {}", requestId, userId);
        return eventService.cancelParticipation(requestId, userId);
    }
}
