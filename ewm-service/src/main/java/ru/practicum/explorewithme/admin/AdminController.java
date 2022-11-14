package ru.practicum.explorewithme.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.CategoryService;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.events.EventService;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.text.ParseException;
import java.util.Collection;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/admin")
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;

    @PostMapping("/users")
    public UserDto postUsers(@Validated @RequestBody UserDto userDto) {
        log.debug("Получен Post-запрос на добавление пользователя {}", userDto);
        return userService.create(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Получен запрос на удаление пользователя с id: {}.", userId);
        userService.remove(userId);
    }

    @GetMapping("/users")
    public Collection<UserDto> getAll(@RequestParam(name = "ids", defaultValue = "0") Long[] ids,
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
    public void deleteCategory(@PathVariable int catId) {
        log.info("Получен запрос на удаление категории с id: {}.", catId);
        categoryService.remove(catId);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.debug("Получен Patch-запрос на публикацию события {}", eventId);
        return eventService.publish(eventId);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEvent(@RequestBody NewEventDto newEventDto,
                                    @PathVariable long eventId) throws ParseException {
        log.debug("Получен Put-запрос на изменения события {}", eventId);
        return eventService.update(eventId, newEventDto);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto reject(@PathVariable long eventId) {
        log.debug("Получен Patch-запрос на публикацию события {}", eventId);
        return eventService.rejectEvent(eventId);
    }
}
