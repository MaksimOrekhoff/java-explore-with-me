package ru.practicum.explorewithme.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.events.StatusEvent;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created")
    private Date created;
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "requester_id")
    private Long requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StatusEvent status;

}
