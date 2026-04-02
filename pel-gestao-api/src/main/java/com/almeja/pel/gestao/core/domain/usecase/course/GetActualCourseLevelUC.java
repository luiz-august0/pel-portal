package com.almeja.pel.gestao.core.domain.usecase.course;

import com.almeja.pel.gestao.core.domain.entity.LevelEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.gateway.repository.LevelRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetActualCourseLevelUC {

    private final LevelRepositoryGTW levelRepositoryGTW;

    public LevelEntity execute(Integer course, PersonEntity person) {
        return levelRepositoryGTW.findNextLevelByCourseAndPerson(course, person.getId());
    }

}
