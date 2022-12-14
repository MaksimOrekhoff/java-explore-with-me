package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester(Long id);

    Request findByIdAndEventId(Long id, Long userId);

    List<Request> findAllByEventId(Long eventId);
}
