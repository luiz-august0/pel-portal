package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumTransferStatus implements IEnum {

    P("P", "Pendente"),
    A("A", "Aprovado"),
    R("R", "Recusado");

    private final String key;
    private final String value;

    EnumTransferStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumTransferStatus, String> {
        @Override
        public EnumTransferStatus[] getValues() {
            return EnumTransferStatus.values();
        }
    }

    @Override
    public Class<? extends IEnum.Converter<EnumTransferStatus, String>> getConverter() {
        return EnumTransferStatus.Converter.class;
    }

}
