package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.enums.EnumAppException;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.core.gateway.token.RecoveryTokenGTW;
import com.almeja.pel.portal.core.mail.builders.RecoveryMailBuilder;
import com.almeja.pel.portal.core.mail.interfaces.ITemplate;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.SendMailCommand;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class GenerateRecoveryUC {

    private final UserRepository userRepository;
    private final RecoveryMailBuilder recoveryMailBuilder;
    private final RecoveryTokenGTW recoveryTokenGTW;
    private final Mediator mediator;

    public void execute(AuthenticationRecoveryRecord authenticationRecoveryRecord) {
        Optional<UserEntity> optionalUser = userRepository.findByCpf(authenticationRecoveryRecord.cpf());
        if (optionalUser.isEmpty()) throw new AppException(EnumAppException.USER_NOT_FOUND);
        UserEntity user = optionalUser.get();
        if (user.getEmail() == null) throw new AppException(EnumAppException.USER_WITHOUT_EMAIL);
        String recoveryToken = recoveryTokenGTW.generateRecoveryToken(user.getCpf());
        ITemplate iTemplate = recoveryMailBuilder.build(recoveryToken);
        mediator.sendAsync(new SendMailCommand(user.getEmail(), iTemplate.getSubject(), iTemplate.getHtml()));
    }

}
