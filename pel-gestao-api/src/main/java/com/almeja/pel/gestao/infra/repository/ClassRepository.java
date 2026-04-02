package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.ClassEntity;
import com.almeja.pel.gestao.core.dto.interfaces.IClassInfoDTO;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Integer>, ClassRepositoryGTW {

    // Condicoes para uma matricula ser considerada ativa/reservada
    String SUBSCRIBERS_ACTIVE_CONDITIONS = " and matricula.cs_situacao in ('A', 'P') ";

    @Override
    default ClassEntity findAndValidate(Integer classId) {
        Optional<ClassEntity> classOptional = findById(classId);
        if (classOptional.isEmpty()) throw new ValidatorException("Turma não encontrada.");
        return classOptional.get();
    }

    @Override
    @Query(value = "" +
            "select turma.id_turma as id,\n" +
            "       pessoa.nm_pessoa as professorName,\n" +
            "       turma.di_semana as dayOfWeek,\n" +
            "       turma.qt_vagas as availableSlots,\n" +
            "       turma.hr_inicio as startTime,\n" +
            "       turma.hr_termino as endTime,\n" +
            "       turma.nm_turma as className,\n" +
            "       turma.dt_inicio_previsto as plannedStartDate,\n" +
            "       turma.nm_sala as roomName,\n" +
            "       turma.dt_termino_previsto as plannedEndDate,\n" +
            "       coalesce(subscribers, 0) as subscribers\n" +
            "  from turma \n" +
            "  left join lateral (\n" +
            "    select count(*) as subscribers\n" +
            "      from matricula \n" +
            "     where matricula.id_turma = turma.id_turma\n" +
            SUBSCRIBERS_ACTIVE_CONDITIONS +
            "  ) as subscribers on true\n" +
            "  left join publico on publico.id_publico = turma.id_publico\n" +
            "  left join pessoa on pessoa.id_pessoa = turma.id_professor\n" +
            " where 1 > 0\n" +
            "   and turma.id_curso = :courseId\n" +
            "   and turma.id_nivel = :levelId\n" +
            "   and turma.dt_inicio_previsto > :limitDate\n" +
            "   and (turma.in_inativa = false or turma.in_inativa is null) \n" +
            "   and (publico.id_publico is null or (:userAge >= publico.qt_idade_minima and :userAge < publico.qt_idade_maxima))\n" +
            "   and turma.qt_vagas > coalesce(subscribers, 0)" +
            " order by turma.di_semana, turma.hr_inicio ", nativeQuery = true)
    List<IClassInfoDTO> findAllActiveByCourseAndLevel(Integer courseId, Integer levelId, Integer userAge, Date limitDate);

    @Override
    @Query(value = "" +
            "select turma.id_turma as id, \n" +
            "       pessoa.nm_pessoa as professorName, \n" +
            "       turma.di_semana as dayOfWeek, \n" +
            "       turma.qt_vagas as availableSlots, \n" +
            "       turma.hr_inicio as startTime, \n" +
            "       turma.hr_termino as endTime, \n" +
            "       turma.nm_turma as className, \n" +
            "       turma.dt_inicio_previsto as plannedStartDate, \n" +
            "       turma.nm_sala as roomName, \n" +
            "       turma.dt_termino_previsto as plannedEndDate, \n" +
            "       coalesce(subscribers, 0) as subscribers \n" +
            "  from turma  \n" +
            "  left join lateral ( \n" +
            "    select count(*) as subscribers \n" +
            "      from matricula  \n" +
            "     where matricula.id_turma = turma.id_turma \n" +
            SUBSCRIBERS_ACTIVE_CONDITIONS +
            "  ) as subscribers on true \n" +
            "  left join pessoa on pessoa.id_pessoa = turma.id_professor \n" +
            " where 1 > 0 \n" +
            "   and turma.id_curso = :courseId \n" +
            "   and turma.id_nivel = :levelId \n" +
            "   and turma.id_publico = :publicId\n" +
            "   and turma.id_turma <> :classId\n" +
            "   and extract(year from turma.dt_inicio_previsto) = :year \n" +
            "   and (turma.in_inativa = false or turma.in_inativa is null)  \n" +
            " order by turma.di_semana, turma.hr_inicio ", nativeQuery = true)
    List<IClassInfoDTO> findAllActiveForTransfer(Integer courseId, Integer levelId, Integer publicId, Integer classId, Integer year);

    @Override
    @Query(value = "" +
            "select coalesce(count(*), 0) as subscribers\n" +
            "  from matricula \n" +
            " where matricula.id_turma = :classId\n" +
            SUBSCRIBERS_ACTIVE_CONDITIONS, nativeQuery = true)
    Integer getSubscribersByClass(Integer classId);

}
