package com.almeja.pel.portal.integration;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.domain.usecase.user.ChangePasswordByRecoveryUC;
import com.almeja.pel.portal.core.domain.usecase.user.GenerateRecoveryUC;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryRecord;
import com.almeja.pel.portal.core.event.NotifyCreateUpdatePortalUserEvent;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.gateway.token.RecoveryTokenGTW;
import com.almeja.pel.portal.infra.service.mail.MailSenderService;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@DisplayName("Testes de integração de recuperação de senha")
class RecoveryIntegrationTest extends BaseIntegrationTest {

    @Inject
    RegisterUC registerUC;

    @Inject
    GenerateRecoveryUC generateRecoveryUC;

    @Inject
    ChangePasswordByRecoveryUC changePasswordByRecoveryUC;

    @Inject
    UserRepositoryGTW userRepositoryGTW;

    @Inject
    RecoveryTokenGTW recoveryTokenGTW;

    @InjectMock
    MailSenderService mailSenderService;

    @InjectMock
    NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;

    @Test
    @DisplayName("Deve gerar token de recuperação e alterar senha com sucesso")
    void shouldGenerateRecoveryTokenAndChangePasswordSuccessfully() {
        // Given
        String cpf = "11144477735";
        String email = "user@example.com";
        String originalPassword = "SenhaOriginal123!";
        String newPassword = "NovaSenha456!";

        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO(email, cpf);
        userRegisterDTO.setPassword(originalPassword);

        doNothing().when(mailSenderService).send(any(), any(), any());
        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then
        Optional<UserEntity> savedUser = userRepositoryGTW.findByCpf(cpf);
        assertTrue(savedUser.isPresent());
        UserEntity user = savedUser.get();
        assertEquals(email, user.getEmail());
        assertTrue(user.getActive());

        assertTrue(BCrypt.verifyer().verify(originalPassword.toCharArray(), user.getPassword()).verified);

        // Given
        AuthenticationRecoveryRecord recoveryRecord = new AuthenticationRecoveryRecord(cpf);

        // When
        assertDoesNotThrow(() -> generateRecoveryUC.execute(recoveryRecord));

        // Then
        String recoveryToken = recoveryTokenGTW.generateRecoveryToken(cpf);
        assertNotNull(recoveryToken);

        // Given
        AuthenticationRecoveryPasswordRecord passwordRecord = new AuthenticationRecoveryPasswordRecord(recoveryToken, newPassword);

        // When
        assertDoesNotThrow(() -> changePasswordByRecoveryUC.execute(passwordRecord));

        // Then
        Optional<UserEntity> updatedUser = userRepositoryGTW.findByCpf(cpf);
        assertTrue(updatedUser.isPresent());
        UserEntity userWithNewPassword = updatedUser.get();

        assertTrue(BCrypt.verifyer().verify(newPassword.toCharArray(), userWithNewPassword.getPassword()).verified);
        assertFalse(BCrypt.verifyer().verify(originalPassword.toCharArray(), userWithNewPassword.getPassword()).verified);
    }

    @Test
    @DisplayName("Deve validar token de recuperação corretamente")
    void shouldValidateRecoveryTokenCorrectly() {
        // Given
        String cpf = "22255588846";
        String email = "user2@example.com";

        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO(email, cpf);

        doNothing().when(mailSenderService).send(any(), any(), any());
        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then
        Optional<UserEntity> savedUser = userRepositoryGTW.findByCpf(cpf);
        assertTrue(savedUser.isPresent());

        // Given
        String recoveryToken = recoveryTokenGTW.generateRecoveryToken(cpf);
        assertNotNull(recoveryToken);

        // When
        String validatedCpf = recoveryTokenGTW.validateRecoveryToken(recoveryToken);

        // Then
        assertEquals(cpf, validatedCpf);
    }

    private UserRegisterDTO createValidUserRegisterDTO(String email, String cpf) {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setName("Usuario de teste");
        dto.setEmail(email);
        dto.setPassword("SenhaValida123!");
        dto.setBirthDate(Date.from(LocalDate.now().minusYears(25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dto.setPhone("11987654321");
        dto.setCpf(cpf);
        dto.setSpecialNeeds(false);
        dto.setProgramKnowledgeSource(EnumProgramKnowledgeSource.FACEBOOK);
        dto.setProgramKnowledgeSourceOther(null);
        dto.setAuthorizedToken(null);
        return dto;
    }

}
