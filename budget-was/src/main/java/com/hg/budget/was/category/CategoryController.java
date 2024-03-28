package com.hg.budget.was.category;

import com.hg.budget.application.category.CategoryCommandService;
import com.hg.budget.application.category.CategoryQueryService;
import com.hg.budget.was.category.command.CreateCategoryCommand;
import com.hg.budget.was.category.response.CategoryResponse;
import com.hg.budget.was.core.response.OkResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    @PostMapping
    public void createCategory(@Valid @RequestBody CreateCategoryCommand command) {
        categoryCommandService.createCategory(command.name());
    }

    @GetMapping
    public OkResponse<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> categories = categoryQueryService.findCategories().stream()
            .map(categoryDto -> new CategoryResponse(categoryDto.id(), categoryDto.name()))
            .toList();
        return new OkResponse<>(categories);
    }
}
