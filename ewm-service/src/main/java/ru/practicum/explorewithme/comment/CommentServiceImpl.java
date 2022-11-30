package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.MyPageRequest;
import ru.practicum.explorewithme.events.Event;
import ru.practicum.explorewithme.events.EventRepository;
import ru.practicum.explorewithme.events.StatusEvent;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.dto.UserDtoShort;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) throws ValidationException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        if (event.getState().equals(StatusEvent.PENDING)) {
            throw new ValidationException("Возможность комментирования закрыта.");
        }
        Comment comment = commentRepository.save(new Comment(null,
                newCommentDto.getComment(), new Date(), userId, event, new ArrayList<>()));
        log.debug("Добавлен комментарий {}", comment);
        return getCommentDto(user, comment);
    }

    @Override
    public CommentDto addToComment(Long userId, Long eventId, Long commentId,
                                   NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Такой комментарий не существует."));
        Comment newComment = commentRepository.save(new Comment(null,
                newCommentDto.getComment(), new Date(), userId, event, new ArrayList<>()));
        comment.getComments().add(newComment);
        comment.setComments(comment.getComments());
        commentRepository.save(newComment);
        log.debug("Добавлен комментарий {} к комментарию {}", newComment, comment);
        return getCommentDto(user, newComment);
    }

    @Override
    public void removeComment(Long userId, Long eventId, Long commentId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Такой комментарий не существует."));
        commentRepository.deleteById(commentId);
        log.debug("Удален комментарий {} ", commentId);
    }

    @Override
    public void removeCommentAdmin(Long commentId) {
        commentRepository.deleteById(commentId);
        log.debug("Удален комментарий {} ", commentId);
    }

    @Override
    public CommentDto getCommentUserById(Long userId, Long eventId, Long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Такой комментарий не существует."));
        log.debug("Получен комментарий {} ", comment);
        return getCommentDto(user, comment);
    }

    @Override
    public List<CommentDto> getAllCommentsUser(Long userId, Integer from, Integer size) {
        MyPageRequest myPageRequest = new MyPageRequest(from, size, Sort.by("created"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        List<Comment> comments = commentRepository.findAllByUserId(userId, myPageRequest);
        log.debug("Получены комментарии {} ", comments);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment c : comments) {
            commentDtos.add(getCommentDto(user, c));
        }
        return commentDtos;
    }

    @Override
    public List<CommentDto> getAllCommentsEvent(Long userId, Long eventId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        MyPageRequest myPageRequest = new MyPageRequest(from, size, Sort.by("created"));
        List<Comment> comments = commentRepository.findAllByEventId(eventId, myPageRequest);
        List<CommentDto> commentDtos = new ArrayList<>();
        log.debug("Получены комментарии {} ", comments);
        for (Comment c : comments) {
            commentDtos.add(getCommentDto(userRepository.findById(c.getUserId()).get(), c));
        }
        return commentDtos;
    }

    @Override
    public List<CommentDto> getSearchCommentsEvent(Long userId, Long eventId, String text,
                                                   Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не существует."));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        MyPageRequest myPageRequest = new MyPageRequest(from, size, Sort.by("created"));
        List<Comment> comments = commentRepository.findAllByEventIdAndCommentContaining(eventId,
                text, myPageRequest);
        List<CommentDto> commentDtos = new ArrayList<>();
        log.debug("Получены комментарии {} ", comments);
        for (Comment c : comments) {
            commentDtos.add(getCommentDto(userRepository.findById(c.getUserId()).get(), c));
        }
        return commentDtos;
    }

    private static CommentDto getCommentDto(User user, Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getComment(),
                new UserDtoShort(user.getId(), user.getName()),
                comment.getCreated(),
                new ArrayList<>());
    }

}
