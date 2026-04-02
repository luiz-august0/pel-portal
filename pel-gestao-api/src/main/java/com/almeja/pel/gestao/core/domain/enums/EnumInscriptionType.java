package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumInscriptionType implements IEnum {

    N("N", "Normal");

    private final String key;
    private final String value;

    EnumInscriptionType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumInscriptionType, String> {
        @Override
        public EnumInscriptionType[] getValues() {
            return EnumInscriptionType.values();
        }
    }

    @Override
    public Class<? extends IEnum.Converter<EnumInscriptionType, String>> getConverter() {
        return EnumInscriptionType.Converter.class;
    }

}
