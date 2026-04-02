package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.AttendanceEntity;

import java.util.List;

public interface AttendanceRepositoryGTW {

    List<AttendanceEntity> findByLessonPlanId(Integer lessonPlanId);

    List<AttendanceEntity> findAllByInscriptionId(Integer inscriptionId);

    AttendanceEntity findAndValidate(Integer attendanceId);

}
