package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.ClassEntity;
import com.almeja.pel.gestao.core.dto.interfaces.IClassInfoDTO;

import java.util.Date;
import java.util.List;

public interface ClassRepositoryGTW {

    List<ClassEntity> findByInactiveFalse();

    List<ClassEntity> findByCourseId(Integer courseId);

    ClassEntity findAndValidate(Integer classId);

    List<IClassInfoDTO> findAllActiveByCourseAndLevel(Integer courseId, Integer levelId, Integer userAge, Date limitDate);

    List<IClassInfoDTO> findAllActiveForTransfer(Integer courseId, Integer levelId, Integer publicId, Integer classId, Integer year);

    Integer getSubscribersByClass(Integer classId);

}
