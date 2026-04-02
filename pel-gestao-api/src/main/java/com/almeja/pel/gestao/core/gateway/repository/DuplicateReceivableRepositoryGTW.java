package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.DuplicateReceivableEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;

import java.util.List;

public interface DuplicateReceivableRepositoryGTW {

    List<DuplicateReceivableEntity> findAllByInscription(InscriptionEntity inscription);

    DuplicateReceivableEntity findAndValidate(Integer duplicateReceivableId);

}
