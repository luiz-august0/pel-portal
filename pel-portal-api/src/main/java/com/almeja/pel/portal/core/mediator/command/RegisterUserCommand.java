package com.almeja.pel.portal.core.mediator.command;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.mediator.Command;

/**
 * Command para processamento pós-registro de usuário
 */
public record RegisterUserCommand(
        UserEntity user,
        String authorizedToken,
        boolean generateResponsibleLink) implements Command<Void> {
}
