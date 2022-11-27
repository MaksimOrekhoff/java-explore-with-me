package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    private Long id;
    @NotBlank(message = "Аннотация не может быть пустой")
    private String annotation;
    private Integer category;
    @NotBlank(message = "Заполните описание")
    private String description;
    @NotBlank
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    @Value("0")
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(message = "Заполните название")
    private String title;
}
