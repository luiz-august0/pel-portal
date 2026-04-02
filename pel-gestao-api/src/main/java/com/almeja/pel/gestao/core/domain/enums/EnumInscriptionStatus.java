package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumInscriptionStatus implements IEnum {

    A("A", "Ativo"),
    D("D", "Desistente"),
    T("T", "Transferido"),
    P("P", "Pendente"),
    C("C", "Cancelado");

    private final String key;
    private final String value;

    EnumInscriptionStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumInscriptionStatus, String> {
        @Override
        public EnumInscriptionStatus[] getValues() {
            return EnumInscriptionStatus.values();
        }
    }

    @Override
    public Class<? extends IEnum.Converter<EnumInscriptionStatus, String>> getConverter() {
        return EnumInscriptionStatus.Converter.class;
    }

}
