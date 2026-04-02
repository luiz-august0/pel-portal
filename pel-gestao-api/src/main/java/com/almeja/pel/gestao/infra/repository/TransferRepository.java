package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.TransferEntity;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.TransferRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Integer>, TransferRepositoryGTW {

    @Override
    default TransferEntity findAndValidate(Integer transferId) {
        Optional<TransferEntity> transferOptional = findById(transferId);
        if (transferOptional.isEmpty()) throw new ValidatorException("Transferência não encontrada.");
        return transferOptional.get();
    }

    @Override
    @Query(value = "" +
            "select extract(year from transferencia.dt_solicitacao)::text " +
            "  from transferencia " +
            " inner join matricula on matricula.id_matricula = transferencia.id_matricula_origem " +
            " where matricula.id_aluno = :personId " +
            " group by extract(year from transferencia.dt_solicitacao) " +
            " order by extract(year from transferencia.dt_solicitacao) desc ", nativeQuery = true)
    List<String> findYearsByPerson(@Param("personId") Integer personId);

    @Override
    @Query(value = "" +
            "select transferencia.* " +
            "  from transferencia " +
            " inner join matricula on matricula.id_matricula = transferencia.id_matricula_origem " +
            " where matricula.id_aluno = :personId " +
            "   and extract(year from transferencia.dt_solicitacao)::text = :year " +
            " order by transferencia.dt_solicitacao desc ", nativeQuery = true)
    List<TransferEntity> findAllByPersonAndYear(Integer personId, String year);

    @Override
    @Query(value = "" +
            "select transferencia.* " +
            "  from transferencia " +
            " inner join matricula on matricula.id_matricula = transferencia.id_matricula_origem " +
            " where matricula.id_matricula = :inscriptionId " +
            "   and transferencia.cs_situacao = 'P' " +
            " limit 1 ", nativeQuery = true)
    TransferEntity findByInscriptionAndStatusPending(Integer inscriptionId);

}
