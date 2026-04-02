package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMinorResponsibleUC {

    private final UserDependentRepositoryGTW userDependentRepositoryGTW;

    public UserDependentEntity execute(UserEntity user) {
        return userDependentRepositoryGTW.findByDependent(user).orElseThrow(() -> new AppException("Não há nenhum responsável"));
    }

}
