package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.DuplicateReceivableEntity;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.DuplicateReceivableRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DuplicateReceivableRepository extends JpaRepository<DuplicateReceivableEntity, Integer>, DuplicateReceivableRepositoryGTW {

    @Override
    default DuplicateReceivableEntity findAndValidate(Integer duplicateReceivableId) {
        Optional<DuplicateReceivableEntity> duplicateReceivableOptional = findById(duplicateReceivableId);
        if (duplicateReceivableOptional.isEmpty()) throw new ValidatorException("Boleto não encontrado.");
        return duplicateReceivableOptional.get();
    }

}
