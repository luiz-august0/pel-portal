package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.dependent.LinkDependentUC;
import com.almeja.pel.portal.core.domain.usecase.user.GenerateResponsibleLinkUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.RegisterUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler para RegisterUserCommand
 */
@Component
@RequiredArgsConstructor
public class RegisterUserCommandHandler implements CommandHandler<RegisterUserCommand, Void> {

    private final LinkDependentUC linkDependentUC;
    private final GenerateResponsibleLinkUC generateResponsibleLinkUC;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Void handle(RegisterUserCommand command) {
        linkDependentUC.execute(command.user(), command.authorizedToken());
        if (command.generateResponsibleLink()) generateResponsibleLinkUC.execute(command.user());
        return null;
    }

}
