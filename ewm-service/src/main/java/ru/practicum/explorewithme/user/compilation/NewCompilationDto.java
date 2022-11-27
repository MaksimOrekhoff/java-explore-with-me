package ru.practicum.explorewithme.user.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private Long[] events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
