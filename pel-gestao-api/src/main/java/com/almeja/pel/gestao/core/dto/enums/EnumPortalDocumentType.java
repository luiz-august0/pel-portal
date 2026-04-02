package com.almeja.pel.gestao.core.dto.enums;

import com.almeja.pel.gestao.core.domain.enums.EnumFileType;
import lombok.Getter;

@Getter
public enum EnumPortalDocumentType {
    DOCUMENT_WITH_PHOTO("DOCUMENT_WITH_PHOTO", "Documento com foto", EnumFileType.CPF),
    PROOF_OF_INTERNAL_RELATIONSHIP("PROOF_OF_INTERNAL_RELATIONSHIP", "Comprovante de vínculo interno", EnumFileType.CI),
    MEDICAL_REPORT("MEDICAL_REPORT", "Laudo médico", EnumFileType.LM);

    private final String key;
    private final String value;
    private final EnumFileType fileType;

    EnumPortalDocumentType(String key, String value, EnumFileType fileType) {
        this.key = key;
        this.value = value;
        this.fileType = fileType;
    }

}
