package com.almeja.pel.portal.core.domain.enums;

import com.almeja.pel.portal.core.domain.entity.UserDetailsEntity;
import com.almeja.pel.portal.core.domain.enums.base.IEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum EnumDocumentType implements IEnum {
    DOCUMENT_WITH_PHOTO("DOCUMENT_WITH_PHOTO", "Documento com foto"),
    MEDICAL_REPORT("MEDICAL_REPORT", "Laudo médico"),
    PROOF_OF_INTERNAL_RELATIONSHIP("PROOF_OF_INTERNAL_RELATIONSHIP", "Comprovante de vínculo interno");

    private final String key;
    private final String value;

    EnumDocumentType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static List<EnumDocumentType> getRequiredDocuments(UserDetailsEntity userDetails) {
        List<EnumDocumentType> requiredDocuments = new ArrayList<>(List.of(DOCUMENT_WITH_PHOTO));
        if (Boolean.TRUE.equals(userDetails.getSpecialNeeds())) requiredDocuments.add(MEDICAL_REPORT);
        return requiredDocuments;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumDocumentType, String> {

        @Override
        public EnumDocumentType[] getValues() {
            return EnumDocumentType.values();
        }

    }

    @Override
    public Class<? extends IEnum.Converter<EnumDocumentType, String>> getConverter() {
        return EnumDocumentType.Converter.class;
    }

}