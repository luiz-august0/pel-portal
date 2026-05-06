package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticatedRecord;
import com.almeja.pel.portal.core.dto.record.AuthorizedUserRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.gateway.authorization.AuthorizationGTW;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.AuthenticateCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthenticateUC {

    private final Mediator mediator;
    private final AuthorizationGTW authorizationGTW;

    @Transactional
    public AuthenticatedRecord execute(AuthenticateRecord authenticateRecord) {
        AuthorizedUserRecord authorizedUserRecord = authorizationGTW.authorize(authenticateRecord);
        UserEntity user = authorizedUserRecord.user();
        if (!user.getActive()) throw new AppException(EnumAppException.USER_INACTIVE);
        mediator.send(new AuthenticateCommand(user, authenticateRecord.authorizedToken()));
        return new AuthenticatedRecord(authorizedUserRecord.accessToken());
    }

}
