package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.comment.CommentDto;
import ru.practicum.explorewithme.events.StatusEvent;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private long id;
    @NotEmpty(message = "Аннотация не может быть пустой")
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    @NotEmpty(message = "Укажите дату проведения события")
    private String eventDate;
    private UserDtoShort initiator;
    private LocationDto location;
    private Boolean paid;
    private int participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private StatusEvent state;
    @NotEmpty(message = "Заполните название")
    private String title;
    private int views;
    private List<CommentDto> comments;
}
