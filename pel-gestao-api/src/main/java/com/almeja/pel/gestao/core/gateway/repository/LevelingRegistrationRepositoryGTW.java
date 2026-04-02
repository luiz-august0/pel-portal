package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.CourseEntity;
import com.almeja.pel.gestao.core.domain.entity.LevelingRegistrationEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;

import java.util.List;
import java.util.Optional;

public interface LevelingRegistrationRepositoryGTW {

    LevelingRegistrationEntity save(LevelingRegistrationEntity levelingRegistrationEntity);

    void delete(LevelingRegistrationEntity levelingRegistrationEntity);

    boolean existsByPersonAndCourse(PersonEntity person, CourseEntity course);

    Optional<LevelingRegistrationEntity> findById(Integer levelingId);

    List<String> findYearsByPerson(Integer personId);

    List<LevelingRegistrationEntity> findAllByPersonAndYear(Integer personId, String year);

}
