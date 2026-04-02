package com.almeja.pel.gestao.core.domain.factory;

import com.almeja.pel.gestao.core.domain.entity.InscriptionConfigEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.service.GetInscriptionValueService;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionConfigRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.PersonRepositoryGTW;
import com.almeja.pel.gestao.core.util.DateUtil;
import com.almeja.pel.gestao.core.util.NumericUtil;
import com.almeja.pel.gestao.core.util.enums.EnumDateFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class InscriptionFactory {

    private final GetInscriptionValueService getInscriptionValueService;
    private final InscriptionConfigRepositoryGTW inscriptionConfigRepositoryGTW;
    private final PersonRepositoryGTW personRepositoryGTW;
    private final HttpServletRequest request;

    public void fillContract(InscriptionEntity inscription) {
        PersonEntity person = inscription.getStudent();
        InscriptionConfigEntity config = inscriptionConfigRepositoryGTW.getConfig();
        if (inscription.getStudent().isMinor()) {
            person = personRepositoryGTW.findByCpf(inscription.getStudent().getResponsibleCpf()).orElseThrow(() -> new ValidatorException("Responsável não encontrado."));
        }
        String contract = config.getContractText();
        contract = contract.replaceAll("nome_aluno", inscription.getStudent().getName());
        contract = contract.replaceAll("nome_responsavel", person.getName());
        contract = contract.replaceAll("ip_matricula", request.getRemoteAddr());
        contract = contract.replaceAll("nome_curso", inscription.getClazz().getCourse().getCourseName());
        contract = contract.replaceAll("nome_nivel", inscription.getClazz().getLevel().getLevelName());
        contract = contract.replaceAll("nome_turma", inscription.getClazz().getClassName());
        contract = contract.replaceAll("data_inicio_turma", EnumDateFormat.DDMMYYYY.format(inscription.getClazz().getPlannedStartDate()));
        contract = contract.replaceAll("data_matricula", EnumDateFormat.DDMMYYYY.format(inscription.getInscriptionDate()));
        contract = contract.replaceAll("data_termino_turma", EnumDateFormat.DDMMYYYY.format(inscription.getClazz().getPlannedEndDate()));
        contract = contract.replaceAll("hora_inicio", EnumDateFormat.HHMM.format(inscription.getClazz().getStartTime()));
        contract = contract.replaceAll("hora_termino", EnumDateFormat.HHMM.format(inscription.getClazz().getEndTime()));
        contract = contract.replaceAll("ano_curso", DateUtil.getYear(inscription.getClazz().getPlannedStartDate()).toString());
        contract = contract.replaceAll("dia_semana", inscription.getClazz().getExtensionDayOfWeek());
        if (inscription.getPaymentForm() != null) {
            BigDecimal value = getInscriptionValueService.getValue(person);
            BigDecimal installmentValue = value.divide(BigDecimal.valueOf(inscription.getPaymentForm().getInstallments()), 2, RoundingMode.HALF_UP);
            contract = contract.replaceAll("forma_pagamento", inscription.getPaymentForm().getDescription());
            contract = contract.replaceAll("valor_parcela_extenso", NumericUtil.convertPriceToWords(installmentValue));
            contract = contract.replaceAll("valor_parcela", NumericUtil.format(installmentValue));
            contract = contract.replaceAll("valor_curso", NumericUtil.format(value));

        }
        contract = contract.replaceAll("idade_aluno", inscription.getStudent().getAge().toString());
        contract = contract.replaceAll("data_extenso", DateUtil.getExtensionDate(inscription.getInscriptionDate()));
        inscription.setContractText(contract);
    }

}
