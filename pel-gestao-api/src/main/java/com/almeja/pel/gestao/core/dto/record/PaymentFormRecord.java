package com.almeja.pel.gestao.core.dto.record;

import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionPayment;

import java.math.BigDecimal;

public record PaymentFormRecord(
        String name,
        String description,
        EnumInscriptionPayment paymentForm,
        BigDecimal total,
        Integer installments,
        BigDecimal installmentsValue
) {
}
