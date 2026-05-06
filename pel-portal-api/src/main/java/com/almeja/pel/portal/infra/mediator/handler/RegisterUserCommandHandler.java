package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.dependent.LinkDependentUC;
import com.almeja.pel.portal.core.domain.usecase.user.GenerateResponsibleLinkUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.RegisterUserCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RegisterUserCommandHandler implements CommandHandler<RegisterUserCommand, Void> {

    private final LinkDependentUC linkDependentUC;
    private final GenerateResponsibleLinkUC generateResponsibleLinkUC;

    @Override
    @Transactional(Transactional.TxType.MANDATORY)
    public Void handle(RegisterUserCommand command) {
        linkDependentUC.execute(command.user(), command.authorizedToken());
        if (command.generateResponsibleLink()) generateResponsibleLinkUC.execute(command.user());
        return null;
    }

}
