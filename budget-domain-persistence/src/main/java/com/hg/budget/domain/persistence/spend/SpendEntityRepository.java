package com.hg.budget.domain.persistence.spend;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendEntityRepository extends SpendEntityCustomRepository, JpaRepository<SpendEntity, Long> {

    List<SpendEntity> findBySpentUser_AccountId(String id);
}
