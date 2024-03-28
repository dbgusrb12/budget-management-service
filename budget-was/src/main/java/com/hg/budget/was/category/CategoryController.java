package com.hg.budget.was.category;

import com.hg.budget.application.category.CategoryQueryService;
import com.hg.budget.was.category.response.CategoryResponse;
import com.hg.budget.was.core.response.OkResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping
    public OkResponse<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> categories = categoryQueryService.findCategories().stream()
            .map(categoryDto -> new CategoryResponse(categoryDto.id(), categoryDto.name()))
            .toList();
        return new OkResponse<>(categories);
    }
}
