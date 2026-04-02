package com.almeja.pel.portal.infra.mediator.handler;

import com.almeja.pel.portal.core.mediator.AsyncCommandHandler;
import com.almeja.pel.portal.core.mediator.command.SendMailCommand;
import com.almeja.pel.portal.infra.service.mail.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * Handler para SendMailCommand
 */
@Component
@RequiredArgsConstructor
@EnableAsync
public class SendMailCommandHandler implements AsyncCommandHandler<SendMailCommand, Void> {

    private final MailSenderService mailSenderService;

    @Async
    @Override
    public void handleAsync(SendMailCommand command) {
        mailSenderService.send(command.to(), command.subject(), command.html());
    }

}
