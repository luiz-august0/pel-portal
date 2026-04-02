package com.almeja.pel.gestao.core.domain.usecase.leveling;

import com.almeja.pel.gestao.core.domain.entity.CourseEntity;
import com.almeja.pel.gestao.core.domain.entity.LevelingRegistrationEntity;
import com.almeja.pel.gestao.core.domain.entity.LevelingScheduleEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.dto.RegisterLevelingDTO;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.CourseRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.LevelingRegistrationRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.LevelingScheduleRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterLevelingUC {

    private final LevelingRegistrationRepositoryGTW levelingRegistrationRepositoryGTW;
    private final LevelingScheduleRepositoryGTW levelingScheduleRepositoryGTW;
    private final CourseRepositoryGTW courseRepositoryGTW;

    @Transactional
    public Integer execute(PersonEntity person, RegisterLevelingDTO registerLevelingDTO) {
        if (person.isNotReviewed()) throw new ValidatorException("Usuário não pode realizar prova de nivelamento.");
        CourseEntity course = courseRepositoryGTW.findAndValidate(registerLevelingDTO.getCourseId());
        Optional<LevelingScheduleEntity> levelingScheduleOptional = levelingScheduleRepositoryGTW.findAvailableByCourseAndDate(course.getId(), registerLevelingDTO.getLevelingDate());
        if (levelingScheduleOptional.isEmpty()) {
            throw new ValidatorException("Nenhum horário disponível para o curso e a data informada.");
        }
        if (levelingRegistrationRepositoryGTW.existsByPersonAndCourse(person, course)) {
            throw new ValidatorException("Já foi realizada ou agendada uma prova de nivelamento para o curso informado.");
        }
        LevelingRegistrationEntity levelingRegistration = new LevelingRegistrationEntity(person, levelingScheduleOptional.get(), course, registerLevelingDTO.getStudyLanguageTime());
        return levelingRegistrationRepositoryGTW.save(levelingRegistration).getId();
    }

}
