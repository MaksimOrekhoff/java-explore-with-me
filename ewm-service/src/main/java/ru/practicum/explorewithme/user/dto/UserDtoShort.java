package ru.practicum.explorewithme.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDtoShort {
    private Long id;
    @NotBlank(message = "Некорректный формат имени")
    private String name;
}
