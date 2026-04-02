package com.almeja.pel.portal.core.mediator.command;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.mediator.Command;

/**
 * Command para processamento pós-autenticação de usuário
 */
public record AuthenticateCommand(
        UserEntity user,
        String authorizedToken) implements Command<Void> {
}
