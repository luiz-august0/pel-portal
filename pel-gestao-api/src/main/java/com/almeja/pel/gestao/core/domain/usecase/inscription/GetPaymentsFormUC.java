package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumInscriptionPayment;
import com.almeja.pel.gestao.core.domain.service.GetInscriptionValueService;
import com.almeja.pel.gestao.core.dto.record.PaymentFormRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentsFormUC {

    private final GetInscriptionValueService getInscriptionValueService;

    public List<PaymentFormRecord> execute(PersonEntity person) {
        List<PaymentFormRecord> paymentsForm = new ArrayList<>();
        BigDecimal value = getInscriptionValueService.getValue(person);
        paymentsForm.add(new PaymentFormRecord("1x no boleto", "Vencimento em 24h", EnumInscriptionPayment.B1x, value, 1, value));
        paymentsForm.add(new PaymentFormRecord("2x no boleto", "1º vencimento em 24h.\n2º vencimento em 30 dias.", EnumInscriptionPayment.B2x, value,
                2, value.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP)));
        paymentsForm.add(new PaymentFormRecord("3x no boleto", "1º vencimento em 24h.\n2º vencimento em 30 dias.\n3º vencimento em 60 dias.", EnumInscriptionPayment.B3x, value,
                3, value.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP)));
        return paymentsForm;
    }

}
