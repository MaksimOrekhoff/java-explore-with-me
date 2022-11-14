package ru.practicum.explorewithme.user;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.user.dto.UserDto;

@Component
public class MapperUsers {
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
