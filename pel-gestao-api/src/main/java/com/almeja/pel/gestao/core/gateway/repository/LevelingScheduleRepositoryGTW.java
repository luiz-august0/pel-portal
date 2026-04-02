package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.LevelingScheduleEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LevelingScheduleRepositoryGTW {

    List<Date> findAllAvailableHoursByCourse(Integer courseId);

    Optional<LevelingScheduleEntity> findAvailableByCourseAndDate(Integer courseId, Date levelingDate);

    List<String> findAllAvailableDatesByCourseAndDate(Integer courseId, Date levelingDate);

}
