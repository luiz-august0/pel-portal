package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumBondType implements IEnum {

    I("I", "Interno"),
    IAG("IAG", "Interno - Acadêmico Graduação"),
    IAP("IAP", "Interno - Acadêmico Pós-Graduação"),
    IAU("IAU", "Interno - Funcionário Ag. Universitário"),
    IDE("IDE", "Interno - Funcionário Docente Efetivo"),
    IDT("IDT", "Interno - Funcionário Docente Temporário"),
    ISP("ISP", "Interno - Servidor do Estado do Paraná"),
    E("E", "Externo");

    private final String key;
    private final String value;

    EnumBondType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumBondType, String> {

        @Override
        public EnumBondType[] getValues() {
            return EnumBondType.values();
        }

    }

    @Override
    public Class<? extends IEnum.Converter<EnumBondType, String>> getConverter() {
        return EnumBondType.Converter.class;
    }

}
