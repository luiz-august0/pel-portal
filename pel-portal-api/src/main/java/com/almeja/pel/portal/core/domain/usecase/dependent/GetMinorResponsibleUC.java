package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetMinorResponsibleUC {

    @Inject
    UserDependentRepositoryGTW userDependentRepositoryGTW;

    public UserDependentEntity execute(UserEntity user) {
        return userDependentRepositoryGTW.findByDependent(user).orElseThrow(() -> new AppException("Não há nenhum responsável"));
    }

}
