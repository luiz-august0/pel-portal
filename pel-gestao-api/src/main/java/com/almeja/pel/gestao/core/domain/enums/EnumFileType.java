package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumFileType implements IEnum {
    CPF("CPF", "Documento com foto"),
    CI("CI", "Comprovante de vínculo interno"),
    RES("RES", "Documento com foto do responsável"),
    LM("LM", "Laudo médico"),
    CRE("CRE", "CRE"),
    RG("RG", "RG");

    private final String key;
    private final String value;

    EnumFileType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumFileType, String> {

        @Override
        public EnumFileType[] getValues() {
            return EnumFileType.values();
        }

    }

    @Override
    public Class<? extends IEnum.Converter<EnumFileType, String>> getConverter() {
        return EnumFileType.Converter.class;
    }

}
