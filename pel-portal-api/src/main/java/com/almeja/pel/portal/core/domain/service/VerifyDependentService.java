package com.almeja.pel.portal.core.domain.service;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class VerifyDependentService {

    @Inject
    UserRepositoryGTW userRepositoryGTW;
    @Inject
    UserDependentRepositoryGTW userDependentRepositoryGTW;

    public DependentVerifiedRecord verify(UserEntity responsible, UUID userDependentId) {
        UserEntity userDependent = userRepositoryGTW.findById(userDependentId).orElseThrow(() -> new AppException(EnumAppException.USER_NOT_FOUND));
        UserDependentEntity dependentLink = userDependentRepositoryGTW.findByUserAndDependent(responsible, userDependent).orElseThrow(() -> new AppException("Dependente não vinculado ao responsável"));
        return new DependentVerifiedRecord(userDependent, dependentLink);
    }

}
