package ru.practicum.explorewithme.user.compilation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", defaultValue = "false") Boolean pinned,
                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size) throws ParseException {
        log.info("Получен Get-запрос на получение подборок событий с параметром закрепления " +
                "на главной странице {}", pinned);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable @NotBlank Long compId) throws ParseException {
        log.info("Получен Get-запрос на получение подборки событий с id :{}", compId);
        return compilationService.getCompilationId(compId);
    }

}
