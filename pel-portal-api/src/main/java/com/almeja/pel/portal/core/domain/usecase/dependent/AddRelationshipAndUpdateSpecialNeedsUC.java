package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.DependentRelationshipAndSpecialNeedsDTO;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class AddRelationshipAndUpdateSpecialNeedsUC {

    @Inject
    VerifyDependentService verifyDependentService;
    @Inject
    UserRepositoryGTW userRepositoryGTW;
    @Inject
    UserDependentRepositoryGTW userDependentRepositoryGTW;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, DependentRelationshipAndSpecialNeedsDTO dependentRelationshipAndSpecialNeedsDTO) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        dependentVerifiedRecord.dependentLink().updateDependentRelationship(dependentRelationshipAndSpecialNeedsDTO.getDependentRelationship());
        dependentVerifiedRecord.userDependent().getUserDetails().updateSpecialNeeds(dependentRelationshipAndSpecialNeedsDTO.getSpecialNeeds());
        userRepositoryGTW.save(dependentVerifiedRecord.userDependent());
        userDependentRepositoryGTW.save(dependentVerifiedRecord.dependentLink());
    }

}
