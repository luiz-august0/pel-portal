package com.almeja.pel.portal.core.domain.enums;

import com.almeja.pel.portal.core.domain.enums.base.IEnum;
import lombok.Getter;

@Getter
public enum EnumProgramKnowledgeSource implements IEnum {
    FACEBOOK("Facebook", "Facebook"),
    INSTAGRAM("Instagram", "Instagram"),
    GOOGLE("Google", "Google"),
    WHATSAPP("WhatsApp", "WhatsApp"),
    EMAIL("E-mail", "E-mail"),
    OUTRO("Outro", "Outro");

    private final String key;

    private final String value;

    EnumProgramKnowledgeSource(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends IEnum.Converter<EnumProgramKnowledgeSource, String> {

        @Override
        public EnumProgramKnowledgeSource[] getValues() {
            return EnumProgramKnowledgeSource.values();
        }

    }

    @Override
    public Class<? extends IEnum.Converter<EnumProgramKnowledgeSource, String>> getConverter() {
        return EnumProgramKnowledgeSource.Converter.class;
    }

}
