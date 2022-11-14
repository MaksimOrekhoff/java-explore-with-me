package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.user.dto.UserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userDB;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userDB.save(new User(userDto.getId(), userDto.getName(), userDto.getEmail()));
        log.debug("Добавлен пользователь: {}", user);
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

}
