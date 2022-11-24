package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    @NotBlank
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    @NotBlank
    private String eventDate;
    private Long id;
    private UserDtoShort initiator;
    @NotBlank
    private Boolean paid;
    @NotBlank
    private String title;
    private Integer views;
}
