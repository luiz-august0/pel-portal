package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.UserDetailsEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.factory.UserFactory;
import com.almeja.pel.portal.core.domain.service.UserValidatorService;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.gateway.crypt.UserCryptPasswordGTW;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.RegisterUserCommand;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.core.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RegisterUC {

    @Inject
    UserRepository userRepository;
    @Inject
    UserCryptPasswordGTW userCryptPasswordGTW;
    @Inject
    UserFactory userFactory;
    @Inject
    Mediator mediator;

    @Transactional
    public void execute(UserRegisterDTO userRegisterDTO) {
        UserEntity user = userFactory.create(userRegisterDTO.getName(), userRegisterDTO.getEmail(), userRegisterDTO.getCpf(), userRegisterDTO.getPassword(),
                userRegisterDTO.getBirthDate(), userRegisterDTO.getPhone(), userRegisterDTO.getSpecialNeeds(), userRegisterDTO.getProgramKnowledgeSource(),
                userRegisterDTO.getProgramKnowledgeSourceOther()
        );
        UserDetailsEntity userDetails = user.getUserDetails();
        user.setPassword(userCryptPasswordGTW.cryptPassword(user.getPassword()));
        user.setAuthorized(!userDetails.isMinor());
        // Valida campo de origem de conhecimento do programa
        if (StringUtil.isNullOrEmpty(userRegisterDTO.getAuthorizedToken())) {
            UserValidatorService.validateProgramKnowledgeSource(userDetails.getProgramKnowledgeSource(), userDetails.getProgramKnowledgeSourceOther());
        }
        boolean generateResponsibleLink = userDetails.isMinor() && StringUtil.isNullOrEmpty(userRegisterDTO.getAuthorizedToken());
        userRepository.save(user);
        // Dispara o command de criação do usuário para validar o link de autorização, vincular dependentes e gerar link de autorização se necessário
        mediator.send(new RegisterUserCommand(user, userRegisterDTO.getAuthorizedToken(), generateResponsibleLink));
    }

}
