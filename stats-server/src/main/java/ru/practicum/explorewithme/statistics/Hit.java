package ru.practicum.explorewithme.statistics;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@NamedNativeQuery(name = "Hit.getAllUnique",
        query = "select app as app, uri as uri, count (distinct ip) as hits " +
                " from endpoint_hits " +
                "where created between ?1 and ?2" +
                " and uri in ?3 " +
                "group by 1, 2 ",
        resultSetMapping = "Mapping.StatsDto")
@NamedNativeQuery(name = "Hit.getAll",
        query = "select app as app, uri as uri, count (ip) as hits " +
                " from endpoint_hits " +
                "where created between ?1 and ?2" +
                " and uri in ?3 " +
                "group by 1, 2 ",
        resultSetMapping = "Mapping.StatsDto")
@SqlResultSetMapping(name = "Mapping.StatsDto",
        classes = @ConstructorResult(targetClass = StatsDto.class,
                columns = {@ColumnResult(name = "app"),
                        @ColumnResult(name = "uri"),
                        @ColumnResult(name = "hits", type = Integer.class)}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "endpoint_hits", schema = "public")
public class Hit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app")
    private String app;
    @Column(name = "uri")
    private String uri;
    @Column(name = "ip")
    private String ip;
    @Column(name = "created")
    private Date created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Hit hit = (Hit) o;
        return id != null && Objects.equals(id, hit.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
