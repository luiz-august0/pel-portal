package com.almeja.pel.gestao.core.domain.service;

import com.almeja.pel.gestao.core.domain.entity.InscriptionConfigEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumBondType;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionConfigRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class GetInscriptionValueService {

    private final InscriptionConfigRepositoryGTW inscriptionConfigRepositoryGTW;

    public BigDecimal getValue(PersonEntity person) {
        InscriptionConfigEntity config = inscriptionConfigRepositoryGTW.getConfig();
        BigDecimal value = config.getExternalCommunityValue();
        if (!person.getBondType().equals(EnumBondType.E)) value = config.getInternalCommunityValue();
        return value;
    }

}
