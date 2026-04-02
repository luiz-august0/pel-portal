package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.CourseEntity;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.CourseRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Integer>, CourseRepositoryGTW {

    @Override
    default CourseEntity findAndValidate(Integer courseId) {
        Optional<CourseEntity> courseOptional = findById(courseId);
        if (courseOptional.isEmpty()) throw new ValidatorException("Curso não encontrado.");
        return courseOptional.get();
    }

    @Override
    @Query(value = "" +
            "select distinct curso.*\n" +
            "  from turma  \n" +
            " inner join curso on curso.id_curso = turma.id_curso  \n" +
            "  left join lateral ( \n" +
            "    select count(*) as subscribers \n" +
            "      from matricula  \n" +
            "     where matricula.id_turma = turma.id_turma \n" +
            "       and matricula.cs_situacao in ('A', 'P') \n" +
            "  ) as subscribers on true \n" +
            "  left join publico on publico.id_publico = turma.id_publico \n" +
            "  left join pessoa on pessoa.id_pessoa = turma.id_professor \n" +
            " where 1 > 0   \n" +
            "   and turma.dt_inicio_previsto > :limitDate \n" +
            "   and (turma.in_inativa = false or turma.in_inativa is null)  \n" +
            "   and (publico.id_publico is null or (:userAge >= publico.qt_idade_minima and :userAge < publico.qt_idade_maxima)) \n" +
            "   and turma.qt_vagas > coalesce(subscribers, 0) \n" +
            " order by curso.nm_curso ", nativeQuery = true)
    List<CourseEntity> findAllActive(Integer userAge, Date limitDate);

}
