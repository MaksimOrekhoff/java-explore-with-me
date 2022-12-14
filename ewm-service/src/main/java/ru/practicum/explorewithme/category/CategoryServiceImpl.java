package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.client.MyPageRequest;
import ru.practicum.explorewithme.events.Event;
import ru.practicum.explorewithme.events.EventRepository;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = categoryRepository.save(
                new Category(categoryDto.getId(), categoryDto.getName()));
        log.debug("Добавлена категория: {}", category);
        return toCategoryDto(category);
    }

    @Override
    public CategoryDto patch(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new NotFoundException("Такая категория не существует."));
        category.setName(categoryDto.getName());
        Category newCategory = categoryRepository.save(category);
        log.debug("Данные категории {} обновлены ", newCategory);
        return toCategoryDto(newCategory);
    }

    @Override
    public void remove(int catId) {
        List<Event> events = eventRepository.findAllByCategory(catId);
        if (!events.isEmpty()) {
            throw new IllegalCallerException("Запрос приводит к нарушению целостности данных");
        }
        categoryRepository.deleteById(catId);
        log.debug("Удалёна категория с id: {}", catId);
    }

    @Override
    public CategoryDto getCategory(int catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Такая категория не существует."));
        log.debug("Получена категория : {}", category);
        return toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategory(Integer from, Integer size) {
        MyPageRequest myPageRequest = new MyPageRequest(from, size, Sort.unsorted());
        Page<Category> categories = categoryRepository.findAll(myPageRequest);
        log.debug("Получены все категории : {}", categories);
        return categories.stream().map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    private CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
