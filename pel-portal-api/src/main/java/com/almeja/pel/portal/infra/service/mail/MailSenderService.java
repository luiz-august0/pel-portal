package com.almeja.pel.portal.infra.service.mail;

import com.almeja.pel.portal.core.exception.AppException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void send(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            message.setSubject(subject);

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (MessagingException messagingException) {
            Logger.getLogger(MailSenderService.class.getName()).log(Level.SEVERE, messagingException.getMessage());
            throw new AppException("Erro ao enviar email: " + messagingException.getMessage());
        }
    }

}
