package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumDuplicateStatus implements IEnum {

    A("A", "Aberto"),
    L("L", "Liquidado"),
    C("C", "Cancelado");

    private final String key;
    private final String value;

    EnumDuplicateStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumDuplicateStatus, String> {
        @Override
        public EnumDuplicateStatus[] getValues() {
            return EnumDuplicateStatus.values();
        }
    }

    @Override
    public Class<? extends IEnum.Converter<EnumDuplicateStatus, String>> getConverter() {
        return EnumDuplicateStatus.Converter.class;
    }

}
