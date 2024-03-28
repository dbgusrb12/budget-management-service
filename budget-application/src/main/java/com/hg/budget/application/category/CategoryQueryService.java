package com.hg.budget.application.category;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.domain.category.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {

    private final CategoryService categoryService;

    public List<CategoryDto> findCategories() {
        return categoryService.findCategories().stream()
            .map(category -> new CategoryDto(category.getId(), category.getName()))
            .toList();
    }
}
