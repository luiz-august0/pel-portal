package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.CourseEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<InscriptionEntity, Integer>, InscriptionRepositoryGTW {

    @Override
    default InscriptionEntity findAndValidate(Integer inscriptionId) {
        Optional<InscriptionEntity> inscriptionOptional = findById(inscriptionId);
        if (inscriptionOptional.isEmpty()) throw new ValidatorException("Matrícula não encontrada.");
        return inscriptionOptional.get();
    }

    @Override
    @Query(value = "" +
            "select extract(year from turma.dt_inicio_previsto)::text " +
            "  from matricula " +
            " inner join turma on matricula.id_turma = turma.id_turma " +
            " where matricula.id_aluno = :personId " +
            " group by extract(year from turma.dt_inicio_previsto) " +
            " order by extract(year from turma.dt_inicio_previsto) desc ", nativeQuery = true)
    List<String> findYearsByPerson(@Param("personId") Integer personId);

    @Override
    @Query(value = "" +
            "select matricula.* " +
            "  from matricula " +
            " inner join turma on matricula.id_turma = turma.id_turma " +
            " where matricula.id_aluno = :personId " +
            "   and extract(year from turma.dt_inicio_previsto)::text = :year " +
            " order by turma.dt_inicio_previsto desc ", nativeQuery = true)
    List<InscriptionEntity> findAllByPersonAndYear(Integer personId, String year);

    @Override
    @Query(value = "" +
            "select matricula.* " +
            "  from matricula " +
            " inner join turma on matricula.id_turma = turma.id_turma " +
            " where matricula.id_aluno = :personId " +
            "   and matricula.cs_situacao not in ('D', 'C') " +
            " order by turma.dt_inicio_previsto desc " +
            " limit 1 ", nativeQuery = true)
    InscriptionEntity findLastByPerson(Integer personId);

    @Override
    @Query("" +
            "select inscription " +
            "  from InscriptionEntity inscription " +
            " where inscription.student = :person " +
            "   and inscription.clazz.course = :course " +
            "   and inscription.status = 'A' " +
            "   and extract(year from inscription.clazz.plannedStartDate) = extract(year from current_date) ")
    List<InscriptionEntity> findAllByPersonAndCourseAndStatusActiveAndCurrentYear(PersonEntity person, CourseEntity course);

    @Override
    @Query(value = "" +
            "select matricula.* \n" +
            "  from matricula\n" +
            " inner join turma on turma.id_turma = matricula.id_turma\n" +
            " where matricula.id_aluno = :personId\n" +
            "   and matricula.cs_situacao = 'A'\n" +
            "   and matricula.cs_resultado is null\n" +
            "   and exists (\n" +
            "     select 1 from turma as selected_class\n" +
            "      where selected_class.id_turma = :classId\n" +
            "        and '%'||selected_class.di_semana||'%' ilike '%'||turma.di_semana||'%'\n" +
            "        and (selected_class.hr_inicio, selected_class.hr_termino) overlaps (turma.hr_inicio, turma.hr_termino)\n" +
            "   )", nativeQuery = true)
    List<InscriptionEntity> findAllOverlapsByPersonAndClassAndStatusActiveAndCurrentYear(Integer personId, Integer classId);

    @Override
    @Query(value = "" +
            "select matricula.* \n" +
            "  from matricula\n" +
            " where matricula.cs_situacao = 'P'\n" +
            "   and matricula.dt_matricula <= (now() - interval '24 hour')\n" +
            "   and not exists (\n" +
            "     select 1 from duplicata_receber\n" +
            "      where duplicata_receber.id_matricula = matricula.id_matricula\n" +
            "   )", nativeQuery = true)
    List<InscriptionEntity> findAllPendings24HoursWithoutDuplicates();

    @Override
    @Query(value = "" +
            "select matricula.* \n" +
            "  from matricula\n" +
            " inner join turma on matricula.id_turma = turma.id_turma \n" +
            " where matricula.cs_situacao = 'A'\n" +
            "   and matricula.cs_resultado is null\n" +
            "   and matricula.id_aluno = :personId\n" +
            "   and turma.dt_termino_previsto > now()", nativeQuery = true)
    List<InscriptionEntity> findAllActiveToTransferByPerson(Integer personId);

}
