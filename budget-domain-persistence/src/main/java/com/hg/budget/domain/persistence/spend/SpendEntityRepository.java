package com.hg.budget.domain.persistence.spend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendEntityRepository extends SpendEntityCustomRepository, JpaRepository<SpendEntity, Long> {

}
