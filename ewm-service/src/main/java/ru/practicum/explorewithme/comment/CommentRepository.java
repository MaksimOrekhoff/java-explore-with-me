package ru.practicum.explorewithme.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.client.MyPageRequest;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserId(Long userId, MyPageRequest myPageRequest);

    List<Comment> findAllByEventId(Long eventId, MyPageRequest myPageRequest);

    List<Comment> findAllByEventIdAndCommentContaining(Long eventId, String text, MyPageRequest myPageRequest);
}
