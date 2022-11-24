package ru.practicum.explorewithme.statistics;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


/**
 * TODO graduation project
 */

@RestController
@AllArgsConstructor
@Slf4j
public class StatController {
    private final StatsService statsService;

    @PostMapping(path = "/hit")
    public void postHit(@RequestBody HitDto hitDto) throws ParseException {
        log.info("Получен Post-запрос с параметрами {}", hitDto);
        statsService.postHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam(name = "start") String start,
                                   @RequestParam(name = "end") String end,
                                   @RequestParam(name = "uris") String[] uris,
                                   @RequestParam(name = "unique") String unique) throws ParseException {
        log.info("Получен Get-запрос о статистике с параметрами {}, {}, {}, {}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

}
