package com.almeja.pel.gestao.core.dto.enums;

import lombok.Getter;

@Getter
public enum EnumPortalProgramKnowledgeSource {

    FACEBOOK("Facebook", "Facebook"),
    INSTAGRAM("Instagram", "Instagram"),
    GOOGLE("Google", "Google"),
    WHATSAPP("WhatsApp", "WhatsApp"),
    EMAIL("E-mail", "E-mail"),
    OUTRO("Outro", "Outros (sites de notícias)");

    private final String key;
    private final String value;

    EnumPortalProgramKnowledgeSource(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
