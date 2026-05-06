package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class SubmitUserReviewUC {

    private final UserRepository userRepository;

    @Transactional
    public void execute(String cpf, boolean approved) {
        Optional<UserEntity> userOptional = userRepository.findByCpf(cpf);
        if (userOptional.isEmpty()) return;
        UserEntity user = userOptional.get();
        user.setReviewed(approved);
        userRepository.save(user);
    }

}
