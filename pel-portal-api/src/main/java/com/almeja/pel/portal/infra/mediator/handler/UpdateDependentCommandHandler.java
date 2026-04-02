package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.user.UpdateUserUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.UpdateDependentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler para UpdateDependentCommand
 */
@Component
@RequiredArgsConstructor
public class UpdateDependentCommandHandler implements CommandHandler<UpdateDependentCommand, Void> {

    private final UpdateUserUC updateUserUC;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Void handle(UpdateDependentCommand command) {
        updateUserUC.execute(command.dependent(), command.userUpdateDTO());
        return null;
    }

}
