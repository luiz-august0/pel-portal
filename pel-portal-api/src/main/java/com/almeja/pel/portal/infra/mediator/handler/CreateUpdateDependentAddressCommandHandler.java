package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.user.CreateUpdateAddressUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.CreateUpdateDependentAddressCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler para CreateUpdateDependentAddressCommand
 */
@Component
@RequiredArgsConstructor
public class CreateUpdateDependentAddressCommandHandler implements CommandHandler<CreateUpdateDependentAddressCommand, Void> {

    private final CreateUpdateAddressUC createUpdateAddressUC;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Void handle(CreateUpdateDependentAddressCommand command) {
        createUpdateAddressUC.execute(command.dependent(), command.createUpdateAddressDTO());
        return null;
    }

}
