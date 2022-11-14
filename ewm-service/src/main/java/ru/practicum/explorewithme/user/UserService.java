package ru.practicum.explorewithme.user;

import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto create(UserDto userDto);

    void remove(long id);

    Collection<UserDto> getAllUsers(Long[] ids, Integer from, Integer size);

}
