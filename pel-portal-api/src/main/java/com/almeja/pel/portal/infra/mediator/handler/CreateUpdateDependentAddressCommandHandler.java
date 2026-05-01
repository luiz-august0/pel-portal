package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.domain.usecase.user.CreateUpdateAddressUC;
import com.almeja.pel.portal.core.mediator.CommandHandler;
import com.almeja.pel.portal.core.mediator.command.CreateUpdateDependentAddressCommand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUpdateDependentAddressCommandHandler implements CommandHandler<CreateUpdateDependentAddressCommand, Void> {

    @Inject
    CreateUpdateAddressUC createUpdateAddressUC;

    @Override
    @Transactional(Transactional.TxType.MANDATORY)
    public Void handle(CreateUpdateDependentAddressCommand command) {
        createUpdateAddressUC.execute(command.dependent(), command.createUpdateAddressDTO());
        return null;
    }

}
