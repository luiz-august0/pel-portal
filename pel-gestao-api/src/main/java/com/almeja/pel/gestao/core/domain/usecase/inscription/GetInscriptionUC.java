package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetInscriptionUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final ClassRepositoryGTW classRepositoryGTW;

    public InscriptionEntity execute(PersonEntity person, Integer inscriptionId) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        validateInscriptionOwnerService.validateOwner(person, inscription);
        inscription.getClazz().setSubscribers(classRepositoryGTW.getSubscribersByClass(inscription.getClazz().getId()));
        return inscription;
    }

}
