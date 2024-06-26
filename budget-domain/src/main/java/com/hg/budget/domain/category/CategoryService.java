package com.hg.budget.domain.category;

import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.category.port.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final IdGenerator idGenerator;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(String name) {
        final Category duplicatedCategory = findCategory(name);
        if (duplicatedCategory.exist()) {
            return duplicatedCategory;
        }
        final Category category = Category.ofCreated(idGenerator, name);
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
