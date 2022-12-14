package ru.practicum.explorewithme.category;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable @NotBlank int catId) {
        log.info("Получен Get-запрос на получение категории с id: {}.", catId);
        return categoryService.getCategory(catId);
    }

    @GetMapping()
    public List<CategoryDto> allCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен Get-запрос на получение всех категорий");
        return categoryService.getAllCategory(from, size);
    }
}
