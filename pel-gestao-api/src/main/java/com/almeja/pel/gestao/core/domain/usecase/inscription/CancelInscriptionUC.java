package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionStatus;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelInscriptionUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;

    @Transactional
    public void execute(PersonEntity person, Integer inscriptionId) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        inscription.validateStatusPendent();
        validateInscriptionOwnerService.validateOwner(person, inscription);
        inscription.setStatus(EnumInscriptionStatus.C);
        inscription.setObservation("Cancelado pelo contratante");
        inscriptionRepositoryGTW.save(inscription);
    }

}
