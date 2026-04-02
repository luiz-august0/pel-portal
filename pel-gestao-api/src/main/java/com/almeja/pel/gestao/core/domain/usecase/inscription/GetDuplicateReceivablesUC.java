package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.DuplicateReceivableEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.gateway.repository.DuplicateReceivableRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDuplicateReceivablesUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final DuplicateReceivableRepositoryGTW duplicateReceivableRepositoryGTW;

    public List<DuplicateReceivableEntity> execute(PersonEntity person, Integer inscriptionId) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        validateInscriptionOwnerService.validateOwner(person, inscription);
        return duplicateReceivableRepositoryGTW.findAllByInscription(inscription);
    }

}
