package ru.practicum.explorewithme.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.events.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    @NotBlank
    private Long id;
    @NotBlank
    private Boolean pinned;
    @NotBlank
    private String title;
    private List<EventShortDto> events = new ArrayList<>();
}
