package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.service.ValidateInscriptionOwnerService;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.file.FileHandlerGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadInscriptionContractPdfUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final ValidateInscriptionOwnerService validateInscriptionOwnerService;
    private final FileHandlerGTW fileHandlerGTW;

    public byte[] execute(PersonEntity person, Integer inscriptionId) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        validateInscriptionOwnerService.validateOwner(person, inscription);
        if (inscription.getFileContractName() == null) {
            throw new ValidatorException("Não foi gerado pdf do contrato da matricula.");
        }
        return fileHandlerGTW.getFile(inscription.getFileContractName(), inscription.getFileContractS3());
    }

}
