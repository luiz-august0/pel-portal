package com.almeja.pel.portal.core.mediator.command;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.UserUpdateDTO;
import com.almeja.pel.portal.core.mediator.Command;

/**
 * Command para atualizar dados de dependente
 */
public record UpdateDependentCommand(
        UserEntity dependent,
        UserUpdateDTO userUpdateDTO) implements Command<Void> {
}
