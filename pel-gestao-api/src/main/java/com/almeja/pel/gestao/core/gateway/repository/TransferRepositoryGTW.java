package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.TransferEntity;

import java.util.List;

public interface TransferRepositoryGTW {

    TransferEntity findAndValidate(Integer transferId);

    TransferEntity save(TransferEntity transfer);

    List<String> findYearsByPerson(Integer personId);

    List<TransferEntity> findAllByPersonAndYear(Integer personId, String year);

    TransferEntity findByInscriptionAndStatusPending(Integer inscriptionId);

}
