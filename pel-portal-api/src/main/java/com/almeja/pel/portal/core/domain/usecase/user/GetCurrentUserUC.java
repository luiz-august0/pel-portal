package com.almeja.pel.portal.core.domain.usecase.user;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.infra.context.AuthContext;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class GetCurrentUserUC {

    @Inject
    UserRepositoryGTW userRepositoryGTW;

    @Inject
    AuthContext authContext;

    public UserEntity execute() {
        Optional<UserEntity> userOptional = userRepositoryGTW.findById(authContext.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new AppException("Usuário nao encontrado");
        }
        UserEntity user = userOptional.get();
        if (!user.getActive()) {
            throw new AppException(EnumAppException.USER_INACTIVE);
        }
        return user;
    }

}
