package com.almeja.pel.gestao.core.domain.enums;

import lombok.Getter;

@Getter
public enum EnumInscriptionPayment {

    B1x("B1x", "Boleto 1x", 1, "A vista"),
    B2x("B2x", "Boleto 2x", 2, "2 vezes"),
    B3x("B3x", "Boleto 3x", 3, "3 vezes");

    private final String key;
    private final String value;
    private final Integer installments;
    private final String description;

    EnumInscriptionPayment(String key, String value, Integer installments, String description) {
        this.key = key;
        this.value = value;
        this.installments = installments;
        this.description = description;
    }

}
