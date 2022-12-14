package ru.practicum.explorewithme.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    private Long id;
    @NotBlank(message = "Напишите свой комментарий")
    private String comment;
}
