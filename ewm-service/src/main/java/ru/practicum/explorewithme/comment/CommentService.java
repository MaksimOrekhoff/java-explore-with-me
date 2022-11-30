package ru.practicum.explorewithme.comment;


import javax.xml.bind.ValidationException;
import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) throws ValidationException;

    CommentDto addToComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto);

    void removeComment(Long userId, Long eventId, Long commentId);

    void removeCommentAdmin(Long commentId);

    CommentDto getCommentUserById(Long userId, Long eventId, Long commentId);

    List<CommentDto> getAllCommentsUser(Long userId, Integer from, Integer size);

    List<CommentDto> getAllCommentsEvent(Long userId, Long eventId, Integer from, Integer size);

    List<CommentDto> getSearchCommentsEvent(Long userId, Long eventId, String text, Integer from, Integer size);
}
