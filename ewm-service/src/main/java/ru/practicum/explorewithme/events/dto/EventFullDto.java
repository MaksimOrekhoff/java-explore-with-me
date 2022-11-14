package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.events.StatusEvent;
import ru.practicum.explorewithme.locations.LocationDto;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private long id;
    @NotEmpty
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    @NotEmpty
    private String eventDate;
    private UserDtoShort initiator;
    private LocationDto location;
    private Boolean paid;
    private int participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private StatusEvent state;
    @NotEmpty
    private String title;
    private int views;
}
