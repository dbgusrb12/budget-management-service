package com.hg.budget.domain.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Long> {

    CategoryEntity findByName(String name);
}
