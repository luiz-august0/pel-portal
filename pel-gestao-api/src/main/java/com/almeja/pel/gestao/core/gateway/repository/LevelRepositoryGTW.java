package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.LevelEntity;

public interface LevelRepositoryGTW {

    LevelEntity findNextLevelByCourseAndPerson(Integer courseId, Integer personId);

}
