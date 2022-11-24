package ru.practicum.explorewithme.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface HitsRepository extends JpaRepository<Hit, Integer> {
    @Query(nativeQuery = true)
    List<StatsDto> getAllUnique(Date start, Date end, List<String> uri);

    @Query(nativeQuery = true)
    List<StatsDto> getAll(Date start, Date end, List<String> uri);

}
