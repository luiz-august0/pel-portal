package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.LevelEntity;
import com.almeja.pel.gestao.core.gateway.repository.LevelRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Integer>, LevelRepositoryGTW {

    @Override
    @Query(value = "" +
            "  -- Nivel do ultimo curso aprovado\n" +
            "  with last_approved_level as (\n" +
            "    select id_proximo_nivel as next_level\n" +
            "      from nivel\n" +
            "     where nivel.id_nivel = (\n" +
            "       select turma.id_nivel \n" +
            "         from matricula\n" +
            "        inner join turma on turma.id_turma = matricula.id_turma\n" +
            "        where matricula.id_aluno = :personId\n" +
            "          and turma.id_curso = :courseId\n" +
            "          and matricula.cs_resultado = 'A'\n" +
            "        order by matricula.dt_matricula desc limit 1 \n" +
            "     )\n" +
            "  ),\n" +
            "  -- Nivel aprovado de prova de nivelamento\n" +
            "  last_leveling_level as (\n" +
            "    select id_proximo_nivel as next_level\n" +
            "      from nivel\n" +
            "     where id_nivel = (\n" +
            "       select marcacao_nivelamento.id_nivel_aprovado\n" +
            "         from marcacao_nivelamento\n" +
            "        inner join horario_nivelamento \n" +
            "           on horario_nivelamento.id_horario_nivelamento = marcacao_nivelamento.id_horario_nivelamento\n" +
            "        where marcacao_nivelamento.id_pessoa = :personId\n" +
            "          and marcacao_nivelamento.id_curso = :courseId\n" +
            "        order by horario_nivelamento.dt_nivelamento desc limit 1\n" +
            "     ) \n" +
            "  ),\n" +
            "  -- Nivel atual\n" +
            "  actual_level as (\n" +
            "    select case when last_approved_level.next_level is null then last_leveling_level.next_level else last_approved_level.next_level end as next_level\n" +
            "      from last_leveling_level\n" +
            "      left join last_approved_level on true\n" +
            "  )\n" +
            "  select * from nivel\n" +
            "   where nivel.id_nivel = (\n" +
            "     select nivel.id_nivel \n" +
            "       from nivel\n" +
            "       left join actual_level on true\n" +
            "      where nivel.id_curso = :courseId\n" +
            "        -- Pega proximo nivel do nivel atual se houver\n" +
            "        and (nivel.id_nivel = actual_level.next_level or actual_level.next_level is null)\n" +
            "        -- Pega o primeiro nivel que tenha um proximo nivel se nao tiver nivel atual \n" +
            "        and (case when actual_level.next_level is null then nivel.id_proximo_nivel is not null else 1 > 0 end)\n" +
            "      order by nivel.cd_nivel asc limit 1\n" +
            "   )", nativeQuery = true)
    LevelEntity findNextLevelByCourseAndPerson(Integer courseId, Integer personId);

}
