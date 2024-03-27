package com.hg.budget.domain.category;

import com.hg.budget.domain.category.port.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(String name) {
        Category duplicatedCategory = findCategory(name);
        if (duplicatedCategory.exist()) {
            return duplicatedCategory;
        }
        Category category = Category.ofCreated(name);
        categoryRepository.save(category);
        return category;
    }

    public Category findCategory(String name) {
        return categoryRepository.findByName(name);
    }

    public Category findCategory(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }
}
