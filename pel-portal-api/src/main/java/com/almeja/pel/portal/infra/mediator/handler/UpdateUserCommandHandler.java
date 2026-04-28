package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.user.VerifyAndNotifyCreateUpdateUserUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, Void> {

    @Inject
    VerifyAndNotifyCreateUpdateUserUC verifyAndNotifyCreateUpdateUserUC;

    @Override
    public Void handle(UpdateUserCommand command) {
        verifyAndNotifyCreateUpdateUserUC.execute(command.user());
        return null;
    }

}
