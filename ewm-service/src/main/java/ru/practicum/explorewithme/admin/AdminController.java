package ru.practicum.explorewithme.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.CategoryService;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.comment.CommentService;
import ru.practicum.explorewithme.events.EventService;
import ru.practicum.explorewithme.events.StatusEvent;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.user.UserService;
import ru.practicum.explorewithme.compilation.CompilationDto;
import ru.practicum.explorewithme.compilation.CompilationService;
import ru.practicum.explorewithme.compilation.NewCompilationDto;
import ru.practicum.explorewithme.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/admin")
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CommentService commentService;

    @PostMapping("/users")
    public UserDto postUsers(@Validated @RequestBody UserDto userDto) {
        log.debug("Получен Post-запрос на добавление пользователя {}", userDto);
        return userService.create(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable @NotBlank long userId) {
        log.info("Получен запрос на удаление пользователя с id: {}.", userId);
        userService.remove(userId);
    }

    @GetMapping("/users")
    public Collection<UserDto> getAll(@RequestParam(name = "ids", defaultValue = "") Long[] ids,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен Get-запрос на получение всех пользователей.");
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping("/categories")
    public CategoryDto postCategory(@Validated @RequestBody CategoryDto categoryDto) {
        log.debug("Получен Post-запрос на добавление категории {}", categoryDto);
        return categoryService.create(categoryDto);
    }

    @PatchMapping("/categories")
    public CategoryDto patchCategory(@Validated @RequestBody CategoryDto categoryDto) {
        log.debug("Получен Patch-запрос на изменение категории {}", categoryDto);
        return categoryService.patch(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable @NotBlank int catId) {
        log.info("Получен запрос на удаление категории с id: {}.", catId);
        categoryService.remove(catId);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable @NotBlank long eventId) {
        log.debug("Получен Patch-запрос на публикацию события {}", eventId);
        return eventService.publish(eventId);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEvent(@RequestBody NewEventDto newEventDto,
                                    @PathVariable @NotBlank long eventId) throws ParseException {
        log.debug("Получен Put-запрос на изменения события {}", eventId);
        return eventService.update(eventId, newEventDto);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto reject(@PathVariable @NotBlank long eventId) {
        log.debug("Получен Patch-запрос на публикацию события {}", eventId);
        return eventService.rejectEvent(eventId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(name = "users", defaultValue = "") Collection<Long> users,
                                        @RequestParam(name = "states", defaultValue = "") List<StatusEvent> states,
                                        @RequestParam(name = "categories", defaultValue = "") Collection<Integer> categories,
                                        @RequestParam(name = "rangeStart", defaultValue = "1900-11-23 00:48:09") String rangeStart,
                                        @RequestParam(name = "rangeEnd", defaultValue = "2100-11-23 00:48:09") String rangeEnd,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) throws ParseException {
        log.debug("Получен Get-запрос на полную информацию обо всех событиях подходящих под переданные условия.");
        return eventService.getSearchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PostMapping("/compilations")
    public CompilationDto postCompilations(@Validated @RequestBody NewCompilationDto newCompilationDto) throws ParseException {
        log.debug("Получен Post-запрос на добавление подборки событий {}", newCompilationDto);
        return compilationService.createCompilations(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilations(@PathVariable @NotBlank long compId) {
        log.info("Получен запрос на удаление подборки с id: {}.", compId);
        compilationService.removeCompilations(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinTheCompilations(@PathVariable @NotBlank long compId) {
        log.info("Получен запрос на закрепление подборки с id: {}.", compId);
        compilationService.pinCompilations(compId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinTheCompilations(@PathVariable @NotBlank long compId) {
        log.info("Получен запрос на открепление подборки с id: {}.", compId);
        compilationService.unpinCompilations(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void removeEventFromCompilations(@PathVariable @NotBlank long compId,
                                            @PathVariable @NotBlank long eventId) {
        log.info("Получен запрос на удаления события: {} из подборки: {}.", eventId, compId);
        compilationService.removeEventCompilations(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventInCompilations(@PathVariable @NotBlank long compId,
                                       @PathVariable @NotBlank long eventId) {
        log.info("Получен запрос на добавление события : {} в подборку с id: {}.", eventId, compId);
        compilationService.addEvent(compId, eventId);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable @NotBlank long commentId) {
        log.info("Получен Delete-запрос на удаление комментария {} администратором", commentId);
        commentService.removeCommentAdmin(commentId);
    }

}
