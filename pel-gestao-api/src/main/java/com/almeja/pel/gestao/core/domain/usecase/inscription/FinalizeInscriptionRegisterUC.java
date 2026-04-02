package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionPayment;
import com.almeja.pel.gestao.core.domain.factory.InscriptionFactory;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.domain.service.ValidateToRegisterInscription;
import com.almeja.pel.gestao.core.event.DispatchFinalizeInscriptionRegisterEvent;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinalizeInscriptionRegisterUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final InscriptionFactory inscriptionFactory;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final DispatchFinalizeInscriptionRegisterEvent dispatchFinalizeInscriptionRegisterEvent;
    private final ValidateToRegisterInscription validateToRegisterInscription;

    @Transactional
    public void execute(PersonEntity person, Integer inscriptionId, EnumInscriptionPayment inscriptionPayment, boolean acceptContract, boolean acceptImageAuthorization) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        inscription.validateStatusPendent();
        validateInscriptionOwnerService.validateOwner(person, inscription);
        validateToRegisterInscription.validate(inscription.getStudent(), inscription.getClazz());
        if (!acceptContract) throw new ValidatorException("Contrato deve ser aceito.");
        inscription.setPaymentForm(inscriptionPayment);
        inscription.setImageTermDescription(acceptImageAuthorization ? "Concordo" : "Discordo");
        inscriptionFactory.fillContract(inscription);
        inscription.setInscriptionFinalized(true);
        inscriptionRepositoryGTW.save(inscription);
        dispatchFinalizeInscriptionRegisterEvent.send(person, inscription);
    }

}
