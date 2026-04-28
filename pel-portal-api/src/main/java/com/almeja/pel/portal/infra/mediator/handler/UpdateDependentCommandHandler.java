package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.user.UpdateUserUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.UpdateDependentCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateDependentCommandHandler implements CommandHandler<UpdateDependentCommand, Void> {

    @Inject
    UpdateUserUC updateUserUC;

    @Override
    @Transactional(Transactional.TxType.MANDATORY)
    public Void handle(UpdateDependentCommand command) {
        updateUserUC.execute(command.dependent(), command.userUpdateDTO());
        return null;
    }

}
