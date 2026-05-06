package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.dependent.LinkDependentUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.AuthenticateCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthenticateCommandHandler implements CommandHandler<AuthenticateCommand, Void> {

    private final LinkDependentUC linkDependentUC;

    @Override
    @Transactional(Transactional.TxType.MANDATORY)
    public Void handle(AuthenticateCommand command) {
        linkDependentUC.execute(command.user(), command.authorizedToken());
        return null;
    }

}
