package com.almeja.pel.gestao.core.domain.enums;

import com.almeja.pel.gestao.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumStudyLanguageTime implements IEnum {
    ONE_TO_TWO_YEARS("UM_A_DOIS_ANOS", "1 a 2 anos"),
    TWO_TO_THREE_YEARS("DOIS_A_TRES_ANOS", "2 a 3 anos"),
    THREE_PLUS_YEARS("3_ANOS_MAIS", "+3 anos");

    private final String key;
    private final String value;

    EnumStudyLanguageTime(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumStudyLanguageTime, String> {

        @Override
        public EnumStudyLanguageTime[] getValues() {
            return EnumStudyLanguageTime.values();
        }

    }

    @Override
    public Class<? extends IEnum.Converter<EnumStudyLanguageTime, String>> getConverter() {
        return EnumStudyLanguageTime.Converter.class;
    }

}
