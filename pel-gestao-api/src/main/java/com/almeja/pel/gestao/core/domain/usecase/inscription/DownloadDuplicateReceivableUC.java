package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.DuplicateReceivableEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.gateway.repository.DuplicateReceivableRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadDuplicateReceivableUC {

    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final DuplicateReceivableRepositoryGTW duplicateReceivableRepositoryGTW;

    public byte[] execute(PersonEntity person, Integer duplicateReceivableId) {
        DuplicateReceivableEntity duplicateReceivable = duplicateReceivableRepositoryGTW.findAndValidate(duplicateReceivableId);
        validateInscriptionOwnerService.validateOwner(person, duplicateReceivable.getInscription());
        return duplicateReceivable.getBankSlipFile();
    }

}
