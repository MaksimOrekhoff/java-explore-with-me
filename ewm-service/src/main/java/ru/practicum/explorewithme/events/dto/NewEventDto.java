package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.explorewithme.locations.LocationDto;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    private Long id;
    @NotBlank
    private String annotation;
    private int category;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    @Value("0")
    private int participantLimit;
    private Boolean requestModeration;
    @NotBlank
    private String title;
}
