package com.almeja.pel.portal.integration;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.domain.usecase.user.ChangePasswordByRecoveryUC;
import com.almeja.pel.portal.core.domain.usecase.user.GenerateRecoveryUC;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.event.NotifyCreateUpdatePortalUserEvent;
import com.almeja.pel.portal.core.gateway.token.RecoveryTokenGTW;
import com.almeja.pel.portal.core.repository.UserRepository;
import com.almeja.pel.portal.infra.service.mail.MailSenderService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
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

@QuarkusTest
@DisplayName("Testes de integração de recuperação de senha")
class RecoveryIntegrationTest extends BaseIntegrationTest {

    @Inject
    RegisterUC registerUC;

    @Inject
    GenerateRecoveryUC generateRecoveryUC;

    @Inject
    ChangePasswordByRecoveryUC changePasswordByRecoveryUC;

    @Inject
    UserRepository userRepository;

    @Inject
    RecoveryTokenGTW recoveryTokenGTW;

    @InjectMock
    MailSenderService mailSenderService;

    @InjectMock
    NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;

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
        Optional<UserEntity> savedUser = userRepository.findByCpf(cpf);
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
