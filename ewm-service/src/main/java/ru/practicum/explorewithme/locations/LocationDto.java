package ru.practicum.explorewithme.locations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotBlank
    private float lat;
    @NotBlank
    private float lon;
}
