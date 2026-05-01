package com.almeja.pel.portal.core.domain.enums;

import com.almeja.pel.portal.core.domain.enums.base.IEnum;
import com.almeja.pel.portal.core.domain.enums.base.IEnumConverter;
import lombok.Getter;

@Getter
public enum EnumTemplateEmail implements IEnum {

    RECOVERY("RECOVERY", "Recuperação", "Recuperação de senha");

    private final String key;

    private final String value;

    private final String subject;

    EnumTemplateEmail(String key, String value, String subject) {
        this.key = key;
        this.value = value;
        this.subject = subject;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter implements IEnumConverter<EnumTemplateEmail, String> {

        @Override
        public EnumTemplateEmail[] getValues() {
            return EnumTemplateEmail.values();
        }

    }

}
