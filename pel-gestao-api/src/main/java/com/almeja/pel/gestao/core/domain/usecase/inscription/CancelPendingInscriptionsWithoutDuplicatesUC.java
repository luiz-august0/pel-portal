package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionStatus;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelPendingInscriptionsWithoutDuplicatesUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;

    @Transactional
    public void execute() {
        List<InscriptionEntity> inscriptions = inscriptionRepositoryGTW.findAllPendings24HoursWithoutDuplicates();
        for (InscriptionEntity inscription : inscriptions) {
            inscription.setStatus(EnumInscriptionStatus.C);
            inscription.setObservation("Cancelado pelo sistema");
            inscriptionRepositoryGTW.save(inscription);
        }
    }

}
