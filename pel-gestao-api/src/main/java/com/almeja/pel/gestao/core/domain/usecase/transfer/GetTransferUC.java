package com.almeja.pel.gestao.core.domain.usecase.transfer;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.entity.TransferEntity;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.TransferRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTransferUC {

    private final TransferRepositoryGTW transferRepositoryGTW;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final ClassRepositoryGTW classRepositoryGTW;

    public TransferEntity execute(PersonEntity person, Integer transferId) {
        TransferEntity transfer = transferRepositoryGTW.findAndValidate(transferId);
        validateInscriptionOwnerService.validateOwner(person, transfer.getSourceInscription());
        transfer.getDestinationClass().setSubscribers(classRepositoryGTW.getSubscribersByClass(transfer.getDestinationClass().getId()));
        transfer.getSourceInscription().getClazz().setSubscribers(classRepositoryGTW.getSubscribersByClass(transfer.getSourceInscription().getClazz().getId()));
        return transfer;
    }

}
