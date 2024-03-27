package com.hg.budget.domain.category.port;

import com.hg.budget.domain.category.Category;
import java.util.List;

public interface CategoryRepository {

    void save(Category category);

    Category findById(Long id);

    Category findByName(String name);

    List<Category> findAll();
}
