package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.EvaluationSystemEntity;
import com.almeja.pel.gestao.core.gateway.repository.EvaluationSystemRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationSystemRepository extends JpaRepository<EvaluationSystemEntity, Integer>, EvaluationSystemRepositoryGTW {
}
