package ru.practicum.explorewithme.events;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.text.ParseException;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable @NotBlank long eventId, HttpServletRequest request) throws ParseException {
        log.debug("Получен Get-запрос на получение события {}", eventId);
        return eventService.getEventPublic(eventId, request);
    }

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(name = "text", defaultValue = "") String text,
                                         @RequestParam(name = "categories", defaultValue = "") List<Integer> categories,
                                         @RequestParam(name = "paid", defaultValue = "false") Boolean paid,
                                         @RequestParam(name = "rangeStart", defaultValue = "") String rangeStart,
                                         @RequestParam(name = "rangeEnd", defaultValue = "") String rangeEnd,
                                         @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(name = "sort", defaultValue = "") String sort,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                         HttpServletRequest request) throws ParseException {
        log.debug("Получен Get-запрос на полную информацию обо всех событиях подходящих под переданные условия.");
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }
}
