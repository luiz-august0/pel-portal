package com.almeja.pel.portal.core.domain.usecase.user;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class SubmitUserReviewUC {

    @Inject
    UserRepositoryGTW userRepositoryGTW;

    @Transactional
    public void execute(String cpf, boolean approved) {
        Optional<UserEntity> userOptional = userRepositoryGTW.findByCpf(cpf);
        if (userOptional.isEmpty()) return;
        UserEntity user = userOptional.get();
        user.setReviewed(approved);
        userRepositoryGTW.save(user);
    }

}
