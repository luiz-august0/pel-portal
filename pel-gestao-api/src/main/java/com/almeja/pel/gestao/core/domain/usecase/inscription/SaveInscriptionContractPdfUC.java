package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.dto.record.FileUploadedRecord;
import com.almeja.pel.gestao.core.gateway.file.FileHandlerGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveInscriptionContractPdfUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final FileHandlerGTW fileHandlerGTW;

    @Transactional
    public void execute(Integer inscriptionId, byte[] contractPdf) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        if (inscription.getFileContractName() != null) {
            fileHandlerGTW.deleteFile(inscription.getFileContractName(), inscription.getFileContractS3());
        }
        String pdfName = inscriptionId + "_contrato.pdf";
        FileUploadedRecord fileUploadedRecord = fileHandlerGTW.uploadFile(contractPdf, pdfName);
        inscription.setFileContractName(fileUploadedRecord.filename());
        inscription.setFileContractS3(fileUploadedRecord.s3File());
        inscriptionRepositoryGTW.save(inscription);
    }

}
