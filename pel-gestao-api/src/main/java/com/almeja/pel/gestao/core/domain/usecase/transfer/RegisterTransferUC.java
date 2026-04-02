package com.almeja.pel.gestao.core.domain.usecase.transfer;

import com.almeja.pel.gestao.core.domain.entity.ClassEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.entity.TransferEntity;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.TransferRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterTransferUC {

    private final TransferRepositoryGTW transferRepositoryGTW;
    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final ClassRepositoryGTW classRepositoryGTW;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;

    @Transactional
    public Integer execute(PersonEntity person, Integer inscriptionId, Integer classId) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        ClassEntity clazz = classRepositoryGTW.findAndValidate(classId);
        validateInscriptionOwnerService.validateOwner(person, inscription);
        if (!inscription.isValidToTransfer()) throw new ValidatorException("Este curso não pode ser transferido.");
        if (transferRepositoryGTW.findByInscriptionAndStatusPending(inscription.getId()) != null) {
            throw new ValidatorException("Já existe uma transferência em análise para este curso.");
        }
        if (Boolean.TRUE.equals(clazz.getInactive())) {
            throw new ValidatorException("Turma inativa. não é possível realizar transferência.");
        }
        TransferEntity transfer = new TransferEntity(inscription, clazz);
        transferRepositoryGTW.save(transfer);
        return transfer.getId();
    }

}
