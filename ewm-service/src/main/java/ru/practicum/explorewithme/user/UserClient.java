package ru.practicum.explorewithme.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.client.BaseClient;
import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.Map;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getUser(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> getAllUsers(Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", null, parameters);

    }

    public ResponseEntity<Object> addUser(UserDto userDto) {
        return post("", userDto);
    }

    public void deleteUser(long userId) {
        delete("/" + userId);
    }

    public ResponseEntity<Object> patchUser(Long userId, UserDto userDto) {
        return patch("/" + userId, userDto);
    }
}