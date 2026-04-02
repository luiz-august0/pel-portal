package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.GradeEntity;
import com.almeja.pel.gestao.core.dto.interfaces.IGradeQuarterDTO;

import java.util.List;

public interface GradeRepositoryGTW {

    List<GradeEntity> findByInscriptionId(Integer inscriptionId);

    List<GradeEntity> findByCourseEvaluationId(Integer courseEvaluationId);

    GradeEntity findAndValidate(Integer gradeId);

    List<IGradeQuarterDTO> findQuartersByInscription(Integer inscriptionId);

    List<GradeEntity> findAllByInscriptionAndQuarter(Integer inscriptionId, Integer quarter);

}
