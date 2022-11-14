package ru.practicum.explorewithme.category;

import ru.practicum.explorewithme.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto patch(CategoryDto categoryDto);

    void remove(int catId);

    CategoryDto getCategory(int catId);

    List<CategoryDto> getAllCategory(Integer from, Integer size);

}
