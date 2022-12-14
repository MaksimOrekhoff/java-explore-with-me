package ru.practicum.explorewithme.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(message = "Напишите свой комментарий")
    private String comment;
    private UserDtoShort userDtoShort;
    private Date created;
    private List<CommentDto> commentToComment = new ArrayList<>();
}
