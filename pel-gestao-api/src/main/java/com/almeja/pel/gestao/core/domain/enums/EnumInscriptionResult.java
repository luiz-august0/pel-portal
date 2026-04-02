package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumInscriptionResult implements IEnum {

    A("A", "Aprovado"),
    R("R", "Reprovado"),
    RF("RF", "Reprovado por falta");

    private final String key;
    private final String value;

    EnumInscriptionResult(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumInscriptionResult, String> {
        @Override
        public EnumInscriptionResult[] getValues() {
            return EnumInscriptionResult.values();
        }
    }

    @Override
    public Class<? extends IEnum.Converter<EnumInscriptionResult, String>> getConverter() {
        return EnumInscriptionResult.Converter.class;
    }

}
