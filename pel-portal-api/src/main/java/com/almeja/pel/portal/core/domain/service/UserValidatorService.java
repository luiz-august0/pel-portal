package com.almeja.pel.portal.core.domain.service;

import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.exception.ValidatorException;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.util.CpfUtil;
import com.almeja.pel.portal.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidatorService {

    private final UserRepositoryGTW userRepositoryGTW;

    public void validateName(String name) {
        if (StringUtil.isNullOrEmpty(name)) {
            throw new ValidatorException("Nome é obrigatório");
        }

        if (name.length() > 150) {
            throw new ValidatorException("Nome deve ter no máximo 150 caracteres");
        }
    }

    public void validateEmail(String email, UUID id) {
        if (StringUtil.isNullOrEmpty(email)) {
            throw new ValidatorException("Email é obrigatório");
        }

        if (!StringUtil.isValidEmail(email)) {
            throw new ValidatorException("Email deve ter um formato válido");
        }

        if (userRepositoryGTW.existsByEmailAndIdIsNot(email, id)) {
            throw new ValidatorException("Email já está em uso");
        }
    }

    public void validatePassword(String password) {
        if (StringUtil.isNullOrEmpty(password)) {
            throw new ValidatorException("Senha é obrigatória");
        }

        // Regex para senha forte:
        // (?=.*[A-Z]) - pelo menos uma letra maiúscula
        // (?=.*\d) - pelo menos um dígito
        // (?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]) - pelo menos um símbolo especial
        // .{8,} - mínimo 8 caracteres
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";

        if (!password.matches(passwordRegex)) {
            throw new ValidatorException("Senha deve ter no mínimo 8 caracteres, incluindo pelo menos uma letra maiúscula, um número e um símbolo especial");
        }
    }

    public static void validateBirthDate(Date birthDate) {
        if (birthDate == null) {
            throw new ValidatorException("Data de nascimento é obrigatória");
        }

        if (birthDate.after(new Date())) {
            throw new ValidatorException("Data de nascimento não pode ser futura");
        }
    }

    public static void validatePhone(String phone) {
        if (StringUtil.isNullOrEmpty(phone)) {
            throw new ValidatorException("Telefone é obrigatório");
        }

        if (StringUtil.keepOnlyNumbers(phone).length() != 11) {
            throw new ValidatorException("Telefone deve ser valido");
        }
    }

    public void validateCpf(String cpf, UUID id) {
        if (StringUtil.isNullOrEmpty(cpf)) {
            throw new ValidatorException("CPF é obrigatório");
        }

        if (!CpfUtil.isValid(cpf)) {
            throw new ValidatorException("CPF deve ser valido");
        }

        if (userRepositoryGTW.existsByCpfAndIdIsNot(cpf, id)) {
            throw new ValidatorException("CPF já está em uso");
        }
    }

    public static void validateSpecialNeeds(Boolean specialNeeds) {
        if (specialNeeds == null) {
            throw new ValidatorException("Informação sobre necessidades especiais é obrigatória");
        }
    }

    public static void validateProgramKnowledgeSource(EnumProgramKnowledgeSource programKnowledgeSource, String programKnowledgeSourceOther) {
        if (programKnowledgeSource == null) {
            throw new ValidatorException("Origem de conhecimento do programa é obrigatória");
        }

        if (programKnowledgeSource == EnumProgramKnowledgeSource.OUTRO && StringUtil.isNullOrEmpty(programKnowledgeSourceOther)) {
            throw new ValidatorException("Quando origem de conhecimento for 'Outro', o campo de especificação é obrigatório");
        }
    }

}
