package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotBlank(message = "Заполните широту")
    private float lat;
    @NotBlank(message = "Заполните долготу")
    private float lon;
}
