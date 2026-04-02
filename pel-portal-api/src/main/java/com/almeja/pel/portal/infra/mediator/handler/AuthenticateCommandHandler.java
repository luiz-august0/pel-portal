package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.dependent.LinkDependentUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.AuthenticateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler para AuthenticateCommand
 */
@Component
@RequiredArgsConstructor
public class AuthenticateCommandHandler implements CommandHandler<AuthenticateCommand, Void> {

    private final LinkDependentUC linkDependentUC;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Void handle(AuthenticateCommand command) {
        linkDependentUC.execute(command.user(), command.authorizedToken());
        return null;
    }

}
