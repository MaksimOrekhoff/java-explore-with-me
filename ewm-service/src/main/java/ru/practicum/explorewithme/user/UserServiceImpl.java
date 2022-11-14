package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.MyPageRequest;
import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userDB;
    private final MapperUsers mapperUsers;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userDB.save(new User(userDto.getId(), userDto.getName(), userDto.getEmail()));
        log.debug("Добавлен пользователь: {}", user);
        return mapperUsers.toUserDto(user);
    }

    @Override
    public void remove(long id) {
        userDB.deleteById(id);
        log.debug("Удалён пользователь с id: {}", id);
    }

    @Override
    public Collection<UserDto> getAllUsers(Long[] ids, Integer from, Integer size) {
        MyPageRequest myPageRequest = new MyPageRequest(from, size, Sort.unsorted());
        Collection<Long> id = new ArrayList<>(List.of(ids));
        return userDB.findAllByIdIn(id, myPageRequest).stream()
                .map(mapperUsers::toUserDto)
                .collect(Collectors.toList());
    }
}
