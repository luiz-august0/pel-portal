package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.CourseEntity;

import java.util.Date;
import java.util.List;

public interface CourseRepositoryGTW {

    List<CourseEntity> findAllActive(Integer userAge, Date limitDate);

    CourseEntity findAndValidate(Integer courseId);

}
