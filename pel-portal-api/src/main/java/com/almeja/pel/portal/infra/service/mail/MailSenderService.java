package com.almeja.pel.portal.infra.service.mail;

import com.almeja.pel.portal.core.exception.AppException;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public class MailSenderService {

    private final Mailer mailer;

    public void send(String to, String subject, String html) {
        try {
            mailer.send(Mail.withHtml(to, subject, html));
        } catch (Exception e) {
            Logger.getLogger(MailSenderService.class.getName()).log(Level.SEVERE, e.getMessage());
            throw new AppException("Erro ao enviar email: " + e.getMessage());
        }
    }

}
