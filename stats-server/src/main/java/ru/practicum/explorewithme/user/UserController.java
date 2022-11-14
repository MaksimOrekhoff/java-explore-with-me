package ru.practicum.explorewithme.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.dto.UserDto;


/**
 * TODO Sprint add-controllers.
 */

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto postUser(@RequestBody UserDto userDto) {
        log.debug("Получен Post-запрос на добавление пользователя");
        return userService.create(userDto);
    }

}
