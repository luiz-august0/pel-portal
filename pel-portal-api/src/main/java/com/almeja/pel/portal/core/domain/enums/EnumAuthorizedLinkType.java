package com.almeja.pel.portal.core.domain.enums;

import lombok.Getter;

@Getter
public enum EnumAuthorizedLinkType {

    RESPONSIBLE("RESPONSIBLE"),
    DEPENDENT("DEPENDENT");

    private final String key;

    EnumAuthorizedLinkType(String key) {
        this.key = key;
    }
    
}
