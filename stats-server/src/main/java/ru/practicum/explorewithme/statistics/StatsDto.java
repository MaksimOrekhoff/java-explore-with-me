package ru.practicum.explorewithme.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatsDto {
    private String app;
    private String uri;
    private Integer hits;
}
