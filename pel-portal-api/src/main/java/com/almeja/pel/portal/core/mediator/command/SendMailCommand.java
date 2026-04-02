package com.almeja.pel.portal.core.mediator.command;

import com.almeja.pel.portal.core.mediator.Command;

/**
 * Command para envio de email
 */
public record SendMailCommand(
        String to,
        String subject,
        String html) implements Command<Void> {
}
