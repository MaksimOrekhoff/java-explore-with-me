package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.CommentDto;
import ru.practicum.explorewithme.comment.CommentService;
import ru.practicum.explorewithme.comment.NewCommentDto;
import ru.practicum.explorewithme.events.EventService;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.dto.UpdateEventRequest;
import ru.practicum.explorewithme.request.RequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.ValidationException;
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
    private final CommentService commentService;

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

    @PostMapping("/{userId}/events/{eventId}/comments")
    public CommentDto postComment(@PathVariable @NotBlank long userId,
                                  @PathVariable @NotBlank long eventId,
                                  @RequestBody @Validated NewCommentDto newCommentDto) throws ValidationException {
        log.info("Получен Post-запрос на добавление пользователем c id: {} событию : {} комментария {}",
                userId, eventId, newCommentDto);
        return commentService.addComment(userId, eventId, newCommentDto);
    }

    @PostMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public CommentDto postAddCommentToComment(@PathVariable @NotBlank long userId,
                                              @PathVariable @NotBlank long eventId,
                                              @PathVariable @NotBlank long commentId,
                                              @RequestBody @Validated NewCommentDto newCommentDto) {
        log.info("Получен Post-запрос на добавление пользователем c id: {} событию : {} комментария {} к комментарию {}",
                userId, eventId, newCommentDto, commentId);
        return commentService.addToComment(userId, eventId, commentId, newCommentDto);
    }

    @DeleteMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public void deleteComment(@PathVariable @NotBlank long userId,
                              @PathVariable @NotBlank long eventId,
                              @PathVariable @NotBlank long commentId) {
        log.info("Получен Delete-запрос на удаление пользователем c id: {} у события : {} комментария {}",
                userId, eventId, commentId);
        commentService.removeComment(userId, eventId, commentId);
    }

    @GetMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public CommentDto getCommentUser(@PathVariable @NotBlank long userId,
                                     @PathVariable @NotBlank long eventId,
                                     @PathVariable @NotBlank long commentId) {
        log.info("Получен Get-запрос на получение комментария {} к событию c id: {} от пользователя: {}",
                commentId, eventId, userId);
        return commentService.getCommentUserById(userId, eventId, commentId);
    }

    @GetMapping("/{userId}/comments")
    public List<CommentDto> getAllCommentsUser(@PathVariable @NotBlank long userId,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен Get-запрос на получение всех комментариев пользователя: {}", userId);
        return commentService.getAllCommentsUser(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}/comments")
    public List<CommentDto> getAllCommentsEvent(@PathVariable @NotBlank long userId,
                                                @PathVariable @NotBlank long eventId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен Get-запрос на получение всех комментариев к событию {} от пользователя: {}", eventId, userId);
        return commentService.getAllCommentsEvent(userId, eventId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}/comments/search")
    public List<CommentDto> searchComments(@PathVariable @NotBlank long userId,
                                           @PathVariable @NotBlank long eventId,
                                           @RequestParam(name = "text", defaultValue = "") String text,
                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен Get-запрос на поиск комментариев к событию {} от пользователя: {} содержащих {}", eventId, userId, text);
        return commentService.getSearchCommentsEvent(userId, eventId, text, from, size);
    }
}
