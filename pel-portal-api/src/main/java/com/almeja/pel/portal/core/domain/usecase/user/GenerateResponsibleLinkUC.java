package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.record.AuthorizedLinkGeneratedRecord;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.gateway.token.AuthorizedLinkGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerateResponsibleLinkUC {

    private final AuthorizedLinkGTW authorizedLinkGTW;
    private final UserRepositoryGTW userRepositoryGTW;

    @Transactional
    public AuthorizedLinkGeneratedRecord execute(UserEntity user) {
        if (!user.getUserDetails().isMinor()) return null;
        AuthorizedLinkGeneratedRecord authorizedLinkGeneratedRecord = authorizedLinkGTW.generateResponsibleLink(user.getCpf());
        user.setResponsibleToken(authorizedLinkGeneratedRecord.token());
        user.setResponsibleTokenGeneratedAt(authorizedLinkGeneratedRecord.generatedAt());
        user.setResponsibleTokenExpiresAt(authorizedLinkGeneratedRecord.expires());
        userRepositoryGTW.save(user);
        return authorizedLinkGeneratedRecord;
    }

}
