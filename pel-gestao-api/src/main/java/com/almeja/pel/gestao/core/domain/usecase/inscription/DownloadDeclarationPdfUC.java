package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.event.AskPersonDeclarationPdfEvent;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import com.almeja.pel.gestao.core.memory.PersonDeclarationMemory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DownloadDeclarationPdfUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final AskPersonDeclarationPdfEvent askPersonDeclarationPdfEvent;

    public byte[] execute(PersonEntity person, Integer inscriptionId) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        validateInscriptionOwnerService.validateOwner(person, inscription);
        askPersonDeclarationPdfEvent.send(inscription.getStudent().getCpf());
        byte[] declarationPdf = null;
        LocalDateTime end = LocalDateTime.now().plusSeconds(30);
        while (LocalDateTime.now().isBefore(end)) {
            declarationPdf = PersonDeclarationMemory.getInstance().getDeclaration(inscription.getStudent().getCpf());
            if (declarationPdf != null) break;
        }
        if (declarationPdf == null) {
            throw new ValidatorException("Não foi possível gerar o pdf da declaração.");
        }
        return declarationPdf;
    }

}
