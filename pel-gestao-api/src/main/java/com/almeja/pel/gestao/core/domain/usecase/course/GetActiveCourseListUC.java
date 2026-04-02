package com.almeja.pel.gestao.core.domain.usecase.course;

import com.almeja.pel.gestao.core.domain.entity.CourseEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionConfigEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.gateway.repository.CourseRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionConfigRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetActiveCourseListUC {

    private final CourseRepositoryGTW courseRepositoryGTW;
    private final InscriptionConfigRepositoryGTW inscriptionConfigRepositoryGTW;

    public List<CourseEntity> execute(PersonEntity person) {
        InscriptionConfigEntity config = inscriptionConfigRepositoryGTW.getConfig();
        return courseRepositoryGTW.findAllActive(person.getAge(), config.getInscriptionStartDate());
    }

}
