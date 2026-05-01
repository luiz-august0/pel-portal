package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.DependentRelationshipAndSpecialNeedsDTO;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.repository.UserDependentRepository;
import com.almeja.pel.portal.core.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class AddRelationshipAndUpdateSpecialNeedsUC {

    @Inject
    VerifyDependentService verifyDependentService;
    @Inject
    UserRepository userRepository;
    @Inject
    UserDependentRepository userDependentRepository;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, DependentRelationshipAndSpecialNeedsDTO dependentRelationshipAndSpecialNeedsDTO) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        dependentVerifiedRecord.dependentLink().updateDependentRelationship(dependentRelationshipAndSpecialNeedsDTO.getDependentRelationship());
        dependentVerifiedRecord.userDependent().getUserDetails().updateSpecialNeeds(dependentRelationshipAndSpecialNeedsDTO.getSpecialNeeds());
        userRepository.save(dependentVerifiedRecord.userDependent());
        userDependentRepository.save(dependentVerifiedRecord.dependentLink());
    }

}
