package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    private int category;
    @Length(min = 20, max = 7000)
    private String description;
    private String eventDate;
    @NotNull
    private Long eventId;
    private Boolean paid;
    @Value("0")
    private int participantLimit;
    @Length(min = 3, max = 120)
    private String title;
}
