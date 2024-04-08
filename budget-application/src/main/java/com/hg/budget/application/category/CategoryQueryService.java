package com.hg.budget.application.category;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.domain.category.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {

    private final CategoryService categoryService;

    public List<CategoryDto> getCategories() {
        return categoryService.findCategories().stream()
            .map(CategoryDto::from)
            .toList();
    }
}
