package ru.practicum.explorewithme.statistics;

import java.text.ParseException;
import java.util.List;

public interface StatsService {
    void postHit(HitDto hitDto) throws ParseException;

    List<StatsDto> getStats(String start, String end, String[] uris, String unique) throws ParseException;

}
