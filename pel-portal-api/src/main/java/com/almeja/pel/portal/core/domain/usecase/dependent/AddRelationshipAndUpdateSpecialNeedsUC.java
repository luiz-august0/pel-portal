package com.almeja.pel.portal.core.domain.usecase.dependent;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.service.VerifyDependentService;
import com.almeja.pel.portal.core.dto.DependentRelationshipAndSpecialNeedsDTO;
import com.almeja.pel.portal.core.dto.record.DependentVerifiedRecord;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddRelationshipAndUpdateSpecialNeedsUC {

    private final VerifyDependentService verifyDependentService;
    private final UserRepositoryGTW userRepositoryGTW;
    private final UserDependentRepositoryGTW userDependentRepositoryGTW;

    @Transactional
    public void execute(UserEntity responsible, UUID userDependentId, DependentRelationshipAndSpecialNeedsDTO dependentRelationshipAndSpecialNeedsDTO) {
        DependentVerifiedRecord dependentVerifiedRecord = verifyDependentService.verify(responsible, userDependentId);
        dependentVerifiedRecord.dependentLink().updateDependentRelationship(dependentRelationshipAndSpecialNeedsDTO.getDependentRelationship());
        dependentVerifiedRecord.userDependent().getUserDetails().updateSpecialNeeds(dependentRelationshipAndSpecialNeedsDTO.getSpecialNeeds());
        userRepositoryGTW.save(dependentVerifiedRecord.userDependent());
        userDependentRepositoryGTW.save(dependentVerifiedRecord.dependentLink());
    }

}
