package com.almeja.pel.gestao.core.domain.usecase.leveling;

import com.almeja.pel.gestao.core.domain.entity.LevelingRegistrationEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.LevelingRegistrationRepositoryGTW;
import com.almeja.pel.gestao.core.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelLevelingUC {

    private final LevelingRegistrationRepositoryGTW levelingRegistrationRepositoryGTW;

    @Transactional
    public void execute(PersonEntity person, Integer levelingId) {
        LevelingRegistrationEntity levelingRegistration = levelingRegistrationRepositoryGTW.findById(levelingId)
                .orElseThrow(() -> new ValidatorException("Agendamento de prova de nivelamento não encontrado."));
        if (!levelingRegistration.getPerson().getId().equals(person.getId())) {
            throw new ValidatorException("Este agendamento não pertence ao usuário.");
        }
        if (levelingRegistration.getApprovedLevel() != null) {
            throw new ValidatorException("Esta prova de nivelamento já foi avaliada.");
        }
        if (levelingRegistration.getLevelingSchedule().getLevelingDate().before(DateUtil.getDate())) {
            throw new ValidatorException("Esta prova de nivelamento já foi realizada ou já expirou a data de agendamento.");
        }
        levelingRegistrationRepositoryGTW.delete(levelingRegistration);
    }

}
