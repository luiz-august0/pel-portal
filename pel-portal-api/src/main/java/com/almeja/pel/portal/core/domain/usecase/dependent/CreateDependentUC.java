package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.factory.UserFactory;
import com.almeja.pel.portal.core.dto.DependentCreateDTO;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.util.DateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class CreateDependentUC {

    @Inject
    UserDependentRepositoryGTW userDependentRepositoryGTW;
    @Inject
    UserFactory userFactory;
    @Inject
    UserRepositoryGTW userRepositoryGTW;

    @Transactional
    public UUID execute(UserEntity responsible, DependentCreateDTO dependentCreateDTO) {
        if (responsible.getUserDetails().isMinor())
            throw new ValidatorException("Menor não pode cadastrar dependentes");
        UserEntity dependent = userFactory.create(dependentCreateDTO.getName(), dependentCreateDTO.getEmail(),
                dependentCreateDTO.getCpf(), dependentCreateDTO.getPassword(), dependentCreateDTO.getBirthDate(),
                dependentCreateDTO.getPhone(), false, responsible.getUserDetails().getProgramKnowledgeSource(),
                responsible.getUserDetails().getProgramKnowledgeSourceOther());
        if (DateUtil.getAge(dependent.getUserDetails().getBirthDate()) >= 18) {
            throw new ValidatorException("Dependente deve ser menor de idade");
        }
        UserEntity dependentSaved = userRepositoryGTW.save(dependent);
        userDependentRepositoryGTW.save(new UserDependentEntity(responsible, dependent, false));
        return dependentSaved.getId();
    }

}
