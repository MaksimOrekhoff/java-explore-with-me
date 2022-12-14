package ru.practicum.explorewithme.events;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.explorewithme.comment.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @Column(name = "category_id")
    private int category;
    @Column(name = "confirmed_Requests")
    private int confirmedRequests;
    @Column(name = "created_On")
    private Date createdOn;
    @Column(name = "description")
    private String description;
    @Column(name = "event_Date")
    private Date eventDate;
    @Column(name = "initiator_id")
    private long initiator;
    @Column(name = "lat")
    private float lat;
    @Column(name = "lon")
    private float lon;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_Limit")
    private int participantLimit;
    @Column(name = "published_On")
    private Date publishedOn;
    @Column(name = "request_Moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "state_event")
    private StatusEvent state;
    @Column(name = "title")
    private String title;
    @Column(name = "views")
    private int views;

    @OneToMany(targetEntity = Comment.class, mappedBy = "event",
            fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", annotation='" + annotation + '\'' +
                ", category=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", createdOn=" + createdOn +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", initiator=" + initiator +
                ", lat=" + lat +
                ", lon=" + lon +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", publishedOn=" + publishedOn +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", title='" + title + '\'' +
                ", views=" + views +
                '}';
    }
}
