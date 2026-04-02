package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.LevelingScheduleEntity;
import com.almeja.pel.gestao.core.gateway.repository.LevelingScheduleRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LevelingScheduleRepository extends JpaRepository<LevelingScheduleEntity, Integer>, LevelingScheduleRepositoryGTW {

    @Override
    @Query(value = "" +
            " select dt_nivelamento from (" +
            "        select distinct horario_nivelamento.dt_nivelamento as dt_nivelamento " +
            "          from horario_nivelamento " +
            "          left join lateral ( " +
            "               select count(*) total_scheduled " +
            "                 from marcacao_nivelamento " +
            "                where marcacao_nivelamento.id_horario_nivelamento = horario_nivelamento.id_horario_nivelamento " +
            "             ) as total_scheduled on true " +
            "         where horario_nivelamento.qt_vagas > total_scheduled " +
            "           and horario_nivelamento.dt_nivelamento > now() " +
            "           and horario_nivelamento.id_curso = :courseId " +
            "           and extract(year from horario_nivelamento.dt_nivelamento) = extract(year from now()) " +
            "      ) order by dt_nivelamento asc ", nativeQuery = true)
    List<Date> findAllAvailableHoursByCourse(@Param("courseId") Integer courseId);

    @Override
    @Query(value = "" +
            " select to_char(dt_nivelamento, 'yyyy-mm-dd') from ( " +
            "        select distinct horario_nivelamento.dt_nivelamento as dt_nivelamento " +
            "          from horario_nivelamento " +
            "          left join lateral ( " +
            "               select count(*) total_scheduled " +
            "                 from marcacao_nivelamento " +
            "                where marcacao_nivelamento.id_horario_nivelamento = horario_nivelamento.id_horario_nivelamento " +
            "             ) as total_scheduled on true " +
            "         where horario_nivelamento.qt_vagas > total_scheduled " +
            "           and horario_nivelamento.dt_nivelamento > now() " +
            "           and horario_nivelamento.id_curso = :courseId " +
            "           and date_part('year', horario_nivelamento.dt_nivelamento) = date_part('year', date(:levelingDate)) " +
            "           and date_part('month', horario_nivelamento.dt_nivelamento) = date_part('month', date(:levelingDate)) " +
            "      ) order by dt_nivelamento asc ", nativeQuery = true)
    List<String> findAllAvailableDatesByCourseAndDate(@Param("courseId") Integer courseId,
                                                      @Param("levelingDate") Date levelingDate);

    @Override
    @Query(value = "" +
            "select horario_nivelamento.* " +
            "  from horario_nivelamento " +
            "  left join lateral ( " +
            "       select count(*) total_scheduled " +
            "         from marcacao_nivelamento " +
            "        where marcacao_nivelamento.id_horario_nivelamento = horario_nivelamento.id_horario_nivelamento " +
            "     ) as total_scheduled on true " +
            " where horario_nivelamento.qt_vagas > total_scheduled " +
            "   and horario_nivelamento.id_curso = :courseId " +
            "   and horario_nivelamento.dt_nivelamento = :levelingDate " +
            " limit 1", nativeQuery = true)
    Optional<LevelingScheduleEntity> findAvailableByCourseAndDate(@Param("courseId") Integer courseId,
                                                                  @Param("levelingDate") Date levelingDate);

}
