package ru.practicum.explorewithme.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "location_id")
    private long location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_Limit")
    private int participantLimit;//request default
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
}
