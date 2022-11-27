package ru.practicum.explorewithme.events;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.client.MyPageRequest;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(long id, PageRequest pageRequest);

    @Query(value = "select * from events where initiator_id in ?1 and state_event in ?2 and category_id in ?3 " +
            "and event_Date between ?4 and ?5 ", nativeQuery = true)
    List<Event> findAdminAllEvens(Collection<Long> initiator, Collection<String> state,
                                  Collection<Integer> category, Date start, Date end,
                                  MyPageRequest myPageRequest);

    Event findByIdAndState(Long eventId, StatusEvent statusEvent);

    List<Event> findAllByIdIn(Collection<Long> id);

    @Query(value =
            "select * from events where state_event like ?1 and (lower(annotation) like lower(?2) " +
                    "or lower(description) like lower(?2)) and category_id in ?3 and event_date between ?4 " +
                    "and ?5 and paid = ?6", nativeQuery = true)
    List<Event> findAllByParam(String statusEvent, String text, Collection<Integer> categories, Date start,
                               Date end, Boolean paid, MyPageRequest myPageRequest);

    @Query(value =
            "select * from events where state_event like ?1 and (lower(annotation) like lower(?2) " +
                    "or lower(description) like lower(?2)) and category_id in ?3 and event_date between ?4 " +
                    "and ?5 and paid = ?5 and (participant_Limit - confirmedRequests) > 0", nativeQuery = true)
    List<Event> findAllByParamOnly(String statusEvent, String text, Collection<Integer> categories, Date start,
                                   Date end, Boolean paid, MyPageRequest myPageRequest);

    List<Event> findAllByCategory(Integer category);
}
