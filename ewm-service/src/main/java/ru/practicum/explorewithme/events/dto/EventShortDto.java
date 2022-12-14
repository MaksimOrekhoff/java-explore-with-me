package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.comment.CommentDto;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    @NotBlank(message = "Аннотация не может быть пустой")
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    @NotBlank(message = "Укажите дату проведения события")
    private String eventDate;
    private Long id;
    private UserDtoShort initiator;
    @NotBlank(message = "Укажите стоимость участия в событии")
    private Boolean paid;
    @NotBlank(message = "Заполните название")
    private String title;
    private Integer views;
    private List<CommentDto> comments;
}
