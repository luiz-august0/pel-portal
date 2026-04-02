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
public class SaveInscriptionCertificatePdfUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final FileHandlerGTW fileHandlerGTW;

    @Transactional
    public void execute(Integer inscriptionId, byte[] certificatePdf) {
        InscriptionEntity inscription = inscriptionRepositoryGTW.findAndValidate(inscriptionId);
        if (inscription.getFileCertificateName() != null) {
            fileHandlerGTW.deleteFile(inscription.getFileCertificateName(), inscription.getFileCertificateS3());
        }
        String pdfName = inscriptionId + "_certificado.pdf";
        FileUploadedRecord fileUploadedRecord = fileHandlerGTW.uploadFile(certificatePdf, pdfName);
        inscription.setFileCertificateName(fileUploadedRecord.filename());
        inscription.setFileCertificateS3(fileUploadedRecord.s3File());
        inscriptionRepositoryGTW.save(inscription);
    }

}
