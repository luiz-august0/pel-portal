package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.InscriptionConfigEntity;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionConfigRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscriptionConfigRepository extends JpaRepository<InscriptionConfigEntity, Integer>, InscriptionConfigRepositoryGTW {

    @Override
    default InscriptionConfigEntity getConfig() {
        return findAll().getFirst();
    }

}
