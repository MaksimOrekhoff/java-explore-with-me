package ru.practicum.explorewithme.statistics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final HitsRepository hitsRepository;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void postHit(HitDto hitDto) throws ParseException {
        Hit hit = hitsRepository.save(new Hit(null, hitDto.getApp(), hitDto.getUri(), hitDto.getIp(), dateFormat.parse(hitDto.getTimestamp())));
        log.info("Сохранен запрос с параметрами {}", hit);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, String[] uris, String unique) throws ParseException {
        List<StatsDto> hits;
        if (unique.equals("true")) {
            hits = hitsRepository.getAllUnique(dateFormat.parse(start), dateFormat.parse(end),
                    List.of(uris));
            System.out.println(hits);
        } else {
            hits = hitsRepository.getAll(dateFormat.parse(start), dateFormat.parse(end), List.of(uris));
        }
        return hits;
    }
}
