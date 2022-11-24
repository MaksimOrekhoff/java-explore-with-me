package ru.practicum.explorewithme.user.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.client.MyPageRequest;
import ru.practicum.explorewithme.events.Event;
import ru.practicum.explorewithme.events.EventRepository;
import ru.practicum.explorewithme.events.MapperEvents;
import ru.practicum.explorewithme.events.dto.EventShortDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.user.UserRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.events.EventServiceImpl.getEventShortDtos;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final MapperEvents mapperEvents;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) throws ParseException {
        MyPageRequest myPageRequest = new MyPageRequest(from, size, Sort.unsorted());
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, myPageRequest);
        if (compilations.isEmpty()) {
            return new ArrayList<>();
        }
        log.debug("Получены подборки {}  ", compilations);
        return toCompilationDtos(compilations);
    }

    @Override
    public CompilationDto createCompilations(NewCompilationDto newCompilationDto) throws ParseException {
        List<Event> events = eventRepository.findAllByIdIn(List.of(newCompilationDto.getEvents()));
        Compilation compilation = compilationRepository.save(new Compilation(null,
                newCompilationDto.getPinned(), newCompilationDto.getTitle(), events));
        List<EventShortDto> shortDtos = toEventShort(compilation.getEvents());
        log.debug("Создана подборка {}  ", compilation);
        return new CompilationDto(compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                shortDtos);
    }

    @Override
    public void removeCompilations(Long compId) {
        compilationRepository.deleteById(compId);
        log.debug("Удалена подборка с id: {}  ", compId);
    }

    @Override
    public CompilationDto getCompilationId(Long compId) throws ParseException {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Такая подборка не существует."));
        CompilationDto compilationDto = new CompilationDto(compilation.getId(), compilation.getPinned(),
                compilation.getTitle(), toEventShort(compilation.getEvents()));
        log.debug("Получена подборка {}  ", compilationDto);
        return compilationDto;
    }

    @Override
    public void pinCompilations(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Такая подборка не существует."));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.debug("На главной странице закреплена подборка {}  ", compilation);
    }

    @Override
    public void unpinCompilations(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Такая подборка не существует."));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.debug("На главной странице откреплена подборка {}  ", compilation);
    }

    @Override
    public void removeEventCompilations(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Такая подборка не существует."));
        List<Event> events = compilation.getEvents().stream()
                .filter(event -> !Objects.equals(event.getId(), eventId)).collect(Collectors.toList());
        compilation.setEvents(events);
        Compilation newCompilation = compilationRepository.save(compilation);
        log.debug("Из подборки {} удалено событие {} ", newCompilation, eventId);

    }

    @Override
    public void addEvent(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Такая подборка не существует."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такое событие не существует."));
        List<Event> events = compilation.getEvents();
        events.add(event);
        compilation.setEvents(events);
        Compilation newCompilation = compilationRepository.save(compilation);
        log.debug("В подборку {} добавлено событие {} ", newCompilation, event);
    }


    private List<EventShortDto> toEventShort(List<Event> events) throws ParseException {
        return getEventShortDtos(events, categoryRepository, userRepository, mapperEvents);
    }

    private List<CompilationDto> toCompilationDtos(List<Compilation> compilations) throws ParseException {
        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            compilationDtos.add(new CompilationDto(compilation.getId(),
                    compilation.getPinned(),
                    compilation.getTitle(),
                    toEventShort(compilation.getEvents())));
        }
        return compilationDtos;
    }
}
