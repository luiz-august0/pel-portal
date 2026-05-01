package com.almeja.pel.portal.core.domain.usecase.dependent;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumAuthorizedLinkType;
import com.almeja.pel.portal.core.dto.record.AuthorizedTokenRecord;
import com.almeja.pel.portal.core.exception.AppException;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.repository.UserDependentRepository;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.core.gateway.token.AuthorizedLinkGTW;
import com.almeja.pel.portal.core.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class LinkDependentUC {

    @Inject
    AuthorizedLinkGTW authorizedLinkGTW;
    @Inject
    UserRepository userRepository;
    @Inject
    UserDependentRepository userDependentRepository;

    @Transactional
    public void execute(UserEntity user, String authorizedToken) {
        if (StringUtil.isNullOrEmpty(authorizedToken)) {
            return;
        }

        AuthorizedTokenRecord authorizedTokenRecord = authorizedLinkGTW.validateToken(authorizedToken);
        UserEntity userThatSendLink = userRepository.findByCpf(authorizedTokenRecord.cpf())
                .orElseThrow(() -> new AppException("Usuário que enviou o link não foi encontrado"));

        if (authorizedTokenRecord.authorizedLinkType().equals(EnumAuthorizedLinkType.RESPONSIBLE)) {
            handleResponsibleLink(user, userThatSendLink);
        } else {
            handleMinorLink(user, userThatSendLink);
        }
    }

    private void handleResponsibleLink(UserEntity responsible, UserEntity minor) {
        if (alreadyLinked(minor, responsible)) return;
        validateResponsibleIsOfLegalAge(responsible);
        responsible.getUserDetails().updateKnowledgeSourceFromOtherUser(minor.getUserDetails());
        minor.setResponsibleToken(null);
        minor.setResponsibleTokenGeneratedAt(null);
        minor.setResponsibleTokenExpiresAt(null);
        userRepository.save(responsible);
        userRepository.save(minor);
        saveDependent(responsible, minor);
    }

    private void handleMinorLink(UserEntity minor, UserEntity responsible) {
        if (alreadyLinked(minor, responsible)) return;
        validateResponsibleIsOfLegalAge(responsible);
        minor.getUserDetails().updateKnowledgeSourceFromOtherUser(responsible.getUserDetails());
        minor.setResponsibleToken(null);
        minor.setResponsibleTokenGeneratedAt(null);
        minor.setResponsibleTokenExpiresAt(null);
        minor.setAuthorized(true);
        userRepository.save(minor);
        saveDependent(responsible, minor);
    }

    private boolean alreadyLinked(UserEntity minor, UserEntity responsible) {
        return userDependentRepository.findByUserAndDependent(responsible, minor).isPresent();
    }

    private void saveDependent(UserEntity user, UserEntity dependent) {
        userDependentRepository.save(new UserDependentEntity(user, dependent, true));
    }

    private void validateResponsibleIsOfLegalAge(UserEntity responsible) {
        if (responsible.getUserDetails().isMinor()) {
            throw new ValidatorException("Responsável deve ser maior de idade");
        }
    }

}
