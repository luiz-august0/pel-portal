package com.almeja.pel.portal.core.domain.factory;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.UserDetailsEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.domain.service.UserValidatorService;
import com.almeja.pel.portal.core.util.Util;
import com.almeja.pel.portal.core.util.enums.EnumDateFormat;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;

@ApplicationScoped
public class UserFactory {

    @Inject
    UserValidatorService userValidatorService;

    public UserEntity create(String name, String email, String cpf, String password, Date birthDate,
                             String phone, Boolean specialNeeds, EnumProgramKnowledgeSource programKnowledgeSource,
                             String programKnowledgeSourceOther) {
        userValidatorService.validateName(name);
        userValidatorService.validateEmail(email, null);
        userValidatorService.validateCpf(cpf, null);
        userValidatorService.validatePassword(password);
        UserDetailsEntity userDetails = new UserDetailsEntity(birthDate, phone, specialNeeds, programKnowledgeSource, programKnowledgeSourceOther);
        return new UserEntity(name, email, cpf, password, userDetails);
    }

    public void update(UserEntity user, String name, String email, String cpf, Date birthDate, String phone) {
        // Validações com o ID do usuário para permitir manter os mesmos dados
        userValidatorService.validateName(name);
        userValidatorService.validateEmail(email, user.getId());
        userValidatorService.validateCpf(cpf, user.getId());
        // Atualizar informações básicas do usuário
        user.updateBasicInfo(name, email, cpf);
        // Atualizar informações de detalhes do usuário
        if (!EnumDateFormat.YYYYMMDD.format((Date) Util.nvl(user.getUserDetails().getBirthDate(), new Date())).equals
                (EnumDateFormat.YYYYMMDD.format((Date) Util.nvl(birthDate, new Date())))) {
            user.setReviewed(false);
        }
        user.getUserDetails().updateBasicInfo(birthDate, phone);
    }

}
