package ru.practicum.explorewithme.comment;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.explorewithme.events.Event;

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
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment")
    private String comment;
    @Column(name = "created")
    private Date created;
    @Column(name = "user_id")
    private Long userId;
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "comments_comments",
            joinColumns = @JoinColumn(name = "comments_id1"),
            inverseJoinColumns = @JoinColumn(name = "comments_id2"))
    private List<Comment> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", created=" + created +
                ", userId=" + userId +
                ", event=" + event +
                '}';
    }
}
