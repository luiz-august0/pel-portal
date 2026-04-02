package com.almeja.pel.portal.core.mediator.command;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.CreateUpdateAddressDTO;
import com.almeja.pel.portal.core.mediator.Command;

/**
 * Command para criar/atualizar endereço de dependente
 */
public record CreateUpdateDependentAddressCommand(
        UserEntity dependent,
        CreateUpdateAddressDTO createUpdateAddressDTO) implements Command<Void> {
}
