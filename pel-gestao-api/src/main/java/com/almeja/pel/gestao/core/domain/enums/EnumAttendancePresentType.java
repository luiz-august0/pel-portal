package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumAttendancePresentType implements IEnum {
    NONE("0", "Nenhum"),
    PRESENT("1", "Presente"),
    ABSENT("2", "Ausente");

    private final String key;
    private final String value;

    EnumAttendancePresentType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumAttendancePresentType, String> {

        @Override
        public EnumAttendancePresentType[] getValues() {
            return EnumAttendancePresentType.values();
        }

    }

    @Override
    public Class<? extends IEnum.Converter<EnumAttendancePresentType, String>> getConverter() {
        return EnumAttendancePresentType.Converter.class;
    }

}
