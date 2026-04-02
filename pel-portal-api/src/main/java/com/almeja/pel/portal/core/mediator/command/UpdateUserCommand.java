package com.almeja.pel.portal.core.mediator.command;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.mediator.Command;

/**
 * Command para processamento pós atualização de usuário
 */
public record UpdateUserCommand(
        UserEntity user) implements Command<Void> {
}
