package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.UserUpdateDTO;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateDependentCommand;
import com.almeja.pel.portal.core.util.DateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class UpdateDependentUC {

    @Inject
    VerifyDependentService verifyDependentService;
    @Inject
    Mediator mediator;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, UserUpdateDTO userUpdateDTO) {
        if (userDependentId != null) {
            DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
            if (userUpdateDTO.getBirthDate() == null || DateUtil.getAge(userUpdateDTO.getBirthDate()) >= 18) {
                throw new ValidatorException("Dependente deve ser menor de idade");
            }
            mediator.send(new UpdateDependentCommand(dependentVerifiedRecord.userDependent(), userUpdateDTO));
        } else {
            throw new AppException("Deve ter um dependente");
        }
    }

}
