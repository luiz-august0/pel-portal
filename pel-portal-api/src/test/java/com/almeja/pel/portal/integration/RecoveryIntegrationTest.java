package com.almeja.pel.portal.integration;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@DisplayName("Testes de integração de recuperação de senha")
class RecoveryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RegisterUC registerUC;

    @Autowired
    private GenerateRecoveryUC generateRecoveryUC;

    @Autowired
    private ChangePasswordByRecoveryUC changePasswordByRecoveryUC;

    @Autowired
    private UserRepositoryGTW userRepositoryGTW;

    @Autowired
    private RecoveryTokenGTW recoveryTokenGTW;

    @MockitoBean
    private MailSenderService mailSenderService;

    @MockitoBean
    private NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;

    @Test
    @DisplayName("Deve gerar token de recuperação e alterar senha com sucesso")
    void shouldGenerateRecoveryTokenAndChangePasswordSuccessfully() {
        // Given - Cria um usuário
        String cpf = "11144477735";
        String email = "user@example.com";
        String originalPassword = "SenhaOriginal123!";
        String newPassword = "NovaSenha456!";

        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO(email, cpf);
        userRegisterDTO.setPassword(originalPassword);

        // Mock do evento de email para não enviar email real
        doNothing().when(mailSenderService).send(any(), any(), any());

        // Mock do notifyCreateUpdatePortalUserEvent para não enviar evento
        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When - Registra o usuário
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then - Verifica se o usuário foi criado
        Optional<UserEntity> savedUser = userRepositoryGTW.findByCpf(cpf);
        assertTrue(savedUser.isPresent());
        UserEntity user = savedUser.get();
        assertEquals(email, user.getEmail());
        assertTrue(user.getActive());

        // Verifica se a senha original está correta
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(originalPassword, user.getPassword()));

        // Given - Gera token de recuperação
        AuthenticationRecoveryRecord recoveryRecord = new AuthenticationRecoveryRecord(cpf);

        // When - Executa geração de token
        assertDoesNotThrow(() -> generateRecoveryUC.execute(recoveryRecord));

        // Then - Gera token manualmente para simular o processo
        String recoveryToken = recoveryTokenGTW.generateRecoveryToken(cpf);
        assertNotNull(recoveryToken);

        // Given - Altera a senha usando o token
        AuthenticationRecoveryPasswordRecord passwordRecord = new AuthenticationRecoveryPasswordRecord(recoveryToken, newPassword);

        // When - Executa mudança de senha
        assertDoesNotThrow(() -> changePasswordByRecoveryUC.execute(passwordRecord));

        // Then - Verifica se a senha foi alterada
        Optional<UserEntity> updatedUser = userRepositoryGTW.findByCpf(cpf);
        assertTrue(updatedUser.isPresent());
        UserEntity userWithNewPassword = updatedUser.get();

        // Verifica se a nova senha está correta
        assertTrue(encoder.matches(newPassword, userWithNewPassword.getPassword()));

        // Verifica se a senha antiga não funciona mais
        assertFalse(encoder.matches(originalPassword, userWithNewPassword.getPassword()));
    }

    @Test
    @DisplayName("Deve validar token de recuperação corretamente")
    void shouldValidateRecoveryTokenCorrectly() {
        // Given - Cria um usuário
        String cpf = "22255588846";
        String email = "user2@example.com";

        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO(email, cpf);

        // Mock do evento de email
        doNothing().when(mailSenderService).send(any(), any(), any());

        // Mock do notifyCreateUpdatePortalUserEvent para não enviar evento
        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When - Registra o usuário
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then - Verifica se o usuário foi criado
        Optional<UserEntity> savedUser = userRepositoryGTW.findByCpf(cpf);
        assertTrue(savedUser.isPresent());

        // Given - Gera token de recuperação
        String recoveryToken = recoveryTokenGTW.generateRecoveryToken(cpf);
        assertNotNull(recoveryToken);

        // When - Valida o token
        String validatedCpf = recoveryTokenGTW.validateRecoveryToken(recoveryToken);

        // Then - Verifica se o CPF retornado é correto
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
