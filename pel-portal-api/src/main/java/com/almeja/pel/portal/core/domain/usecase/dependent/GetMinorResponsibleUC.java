package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.repository.UserDependentRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetMinorResponsibleUC {

    @Inject
    UserDependentRepository userDependentRepository;

    public UserDependentEntity execute(UserEntity user) {
        return userDependentRepository.findByDependent(user).orElseThrow(() -> new AppException("Não há nenhum responsável"));
    }

}
