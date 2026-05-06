package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.mediator.AsyncCommandHandler;
import com.almeja.pel.portal.core.mediator.command.SendMailCommand;
import com.almeja.pel.portal.infra.service.mail.MailSenderService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SendMailCommandHandler implements AsyncCommandHandler<SendMailCommand, Void> {

    private final MailSenderService mailSenderService;

    @Override
    public void handleAsync(SendMailCommand command) {
        mailSenderService.send(command.to(), command.subject(), command.html());
    }

}
