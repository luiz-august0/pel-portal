package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.CourseEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;

import java.util.List;

public interface InscriptionRepositoryGTW {

    InscriptionEntity findAndValidate(Integer inscriptionId);

    List<String> findYearsByPerson(Integer personId);

    List<InscriptionEntity> findAllByPersonAndYear(Integer personId, String year);

    InscriptionEntity findLastByPerson(Integer personId);

    InscriptionEntity save(InscriptionEntity inscription);

    List<InscriptionEntity> findAllByPersonAndCourseAndStatusActiveAndCurrentYear(PersonEntity person, CourseEntity course);

    List<InscriptionEntity> findAllOverlapsByPersonAndClassAndStatusActiveAndCurrentYear(Integer personId, Integer classId);

    List<InscriptionEntity> findAllPendings24HoursWithoutDuplicates();

    List<InscriptionEntity> findAllActiveToTransferByPerson(Integer personId);

}
