package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.GradeEntity;
import com.almeja.pel.gestao.core.dto.interfaces.IGradeQuarterDTO;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.GradeRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, Integer>, GradeRepositoryGTW {

    @Override
    default GradeEntity findAndValidate(Integer gradeId) {
        Optional<GradeEntity> gradeOptional = findById(gradeId);
        if (gradeOptional.isEmpty()) throw new ValidatorException("Nota não encontrada.");
        return gradeOptional.get();
    }

    @Override
    @Query(value = "" +
            "select curso_avaliacao.nr_agrupamento as quarter,\n" +
            "       sum(coalesce(nota.vl_nota, 0)) as grade\n" +
            "  from nota\n" +
            " inner join curso_avaliacao on nota.id_curso_avaliacao = curso_avaliacao.id_curso_avaliacao \n" +
            " where nota.id_matricula = :inscriptionId\n" +
            " group by curso_avaliacao.nr_agrupamento " +
            " order by curso_avaliacao.nr_agrupamento asc", nativeQuery = true)
    List<IGradeQuarterDTO> findQuartersByInscription(Integer inscriptionId);

    @Override
    @Query(value = "" +
            "select nota.*\n" +
            "  from nota\n" +
            " inner join curso_avaliacao on nota.id_curso_avaliacao = curso_avaliacao.id_curso_avaliacao \n" +
            " where nota.id_matricula = :inscriptionId\n" +
            "   and curso_avaliacao.nr_agrupamento = :quarter", nativeQuery = true)
    List<GradeEntity> findAllByInscriptionAndQuarter(Integer inscriptionId, Integer quarter);

}
