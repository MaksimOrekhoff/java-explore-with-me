package ru.practicum.explorewithme.compilation;

import java.text.ParseException;
import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) throws ParseException;

    CompilationDto createCompilations(NewCompilationDto newCompilationDto) throws ParseException;

    void removeCompilations(Long compId);

    CompilationDto getCompilationId(Long compId) throws ParseException;

    void pinCompilations(Long compId);

    void unpinCompilations(Long compId);

    void removeEventCompilations(Long compId, Long eventId);

    void addEvent(Long compId, Long eventId);
}
