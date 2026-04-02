package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmitUserReviewUC {

    private final UserRepositoryGTW userRepositoryGTW;

    @Transactional
    public void execute(String cpf, boolean approved) {
        Optional<UserEntity> userOptional = userRepositoryGTW.findByCpf(cpf);
        if (userOptional.isEmpty()) return;
        UserEntity user = userOptional.get();
        user.setReviewed(approved);
        userRepositoryGTW.save(user);
    }

}
