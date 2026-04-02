package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.LevelingRegistrationEntity;
import com.almeja.pel.gestao.core.gateway.repository.LevelingRegistrationRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelingRegistrationRepository extends JpaRepository<LevelingRegistrationEntity, Integer>, LevelingRegistrationRepositoryGTW {

    @Override
    @Query(value = "" +
            "select extract(year from horario_nivelamento.dt_nivelamento)::text " +
            "  from marcacao_nivelamento " +
            " inner join horario_nivelamento " +
            "    on horario_nivelamento.id_horario_nivelamento = marcacao_nivelamento.id_horario_nivelamento " +
            " where marcacao_nivelamento.id_pessoa = :personId " +
            " group by extract(year from horario_nivelamento.dt_nivelamento) ", nativeQuery = true)
    List<String> findYearsByPerson(@Param("personId") Integer personId);

    @Override
    @Query(value = "" +
            "select marcacao_nivelamento.* " +
            "  from marcacao_nivelamento " +
            " inner join horario_nivelamento " +
            "    on horario_nivelamento.id_horario_nivelamento = marcacao_nivelamento.id_horario_nivelamento " +
            " where marcacao_nivelamento.id_pessoa = :personId " +
            "   and extract(year from horario_nivelamento.dt_nivelamento)::text = :year ", nativeQuery = true)
    List<LevelingRegistrationEntity> findAllByPersonAndYear(Integer personId, String year);
    
}
