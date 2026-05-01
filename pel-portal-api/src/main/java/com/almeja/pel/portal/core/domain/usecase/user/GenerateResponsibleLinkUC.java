package com.almeja.pel.portal.core.domain.usecase.user;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.record.AuthorizedLinkGeneratedRecord;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.core.gateway.token.AuthorizedLinkGTW;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GenerateResponsibleLinkUC {

    @Inject
    AuthorizedLinkGTW authorizedLinkGTW;
    @Inject
    UserRepository userRepository;

    @Transactional
    public AuthorizedLinkGeneratedRecord execute(UserEntity user) {
        if (!user.getUserDetails().isMinor()) return null;
        AuthorizedLinkGeneratedRecord authorizedLinkGeneratedRecord = authorizedLinkGTW.generateResponsibleLink(user.getCpf());
        user.setResponsibleToken(authorizedLinkGeneratedRecord.token());
        user.setResponsibleTokenGeneratedAt(authorizedLinkGeneratedRecord.generatedAt());
        user.setResponsibleTokenExpiresAt(authorizedLinkGeneratedRecord.expires());
        userRepository.save(user);
        return authorizedLinkGeneratedRecord;
    }

}
