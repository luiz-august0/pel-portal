package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.user.VerifyAndNotifyCreateUpdateUserUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handler para UpdateUserCommandHandler
 */
@Component
@RequiredArgsConstructor
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, Void> {

    private final VerifyAndNotifyCreateUpdateUserUC verifyAndNotifyCreateUpdateUserUC;

    @Override
    public Void handle(UpdateUserCommand command) {
        verifyAndNotifyCreateUpdateUserUC.execute(command.user());
        return null;
    }

}
