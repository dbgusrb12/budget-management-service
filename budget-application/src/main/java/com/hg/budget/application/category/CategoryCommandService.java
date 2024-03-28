package com.hg.budget.application.category;

import com.hg.budget.domain.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryService categoryService;

    @Transactional
    public void createCategory(String name) {
        categoryService.createCategory(name);
    }
}
