package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.AddressEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.CreateUpdateDependentAddressDTO;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.CreateUpdateDependentAddressCommand;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import org.hibernate.Hibernate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class CreateUpdateDependentAddressUC {

    @Inject
    VerifyDependentService verifyDependentService;
    @Inject
    UserRepositoryGTW userRepositoryGTW;
    @Inject
    Mediator mediator;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, CreateUpdateDependentAddressDTO createUpdateDependentAddressDTO) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        if (createUpdateDependentAddressDTO.isSameAddress()) {
            if (responsible.getAddress() == null)
                throw new AppException("Responsável não possui endereço cadastrado");
            dependentVerifiedRecord.userDependent().updateAddress((AddressEntity) Hibernate.unproxy(responsible.getAddress()));
            userRepositoryGTW.save(dependentVerifiedRecord.userDependent());
            mediator.send(new UpdateUserCommand(dependentVerifiedRecord.userDependent()));
        } else {
            dependentVerifiedRecord.userDependent().updateAddress(null);
            userRepositoryGTW.save(dependentVerifiedRecord.userDependent());
            mediator.send(new CreateUpdateDependentAddressCommand(dependentVerifiedRecord.userDependent(), createUpdateDependentAddressDTO));
        }
    }

}
