package com.almeja.pel.portal.core.domain.service;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.repository.UserDependentRepository;
import com.almeja.pel.portal.core.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class VerifyDependentService {

    @Inject
    UserRepository userRepository;
    @Inject
    UserDependentRepository userDependentRepository;

    public DependentVerifiedRecord verify(UserEntity responsible, UUID userDependentId) {
        UserEntity userDependent = userRepository.findById(userDependentId).orElseThrow(() -> new AppException(EnumAppException.USER_NOT_FOUND));
        UserDependentEntity dependentLink = userDependentRepository.findByUserAndDependent(responsible, userDependent).orElseThrow(() -> new AppException("Dependente não vinculado ao responsável"));
        return new DependentVerifiedRecord(userDependent, dependentLink);
    }

}
