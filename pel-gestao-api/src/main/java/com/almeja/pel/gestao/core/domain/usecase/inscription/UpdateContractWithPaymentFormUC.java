package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionPayment;
import com.almeja.pel.gestao.core.domain.factory.InscriptionFactory;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateContractWithPaymentFormUC {

    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final InscriptionFactory inscriptionFactory;

    @Transactional
    public String execute(PersonEntity person, Integer inscriptionId, EnumInscriptionPayment inscriptionPayment) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        inscription.validateStatusPendent();
        validateInscriptionOwnerService.validateOwner(person, inscription);
        inscription.setPaymentForm(inscriptionPayment);
        inscriptionFactory.fillContract(inscription);
        inscriptionRepositoryGTW.save(inscription);
        return inscription.getContractText();
    }

}
