package com.almeja.pel.portal.integration;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.domain.usecase.user.AuthenticateUC;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticatedRecord;
import com.almeja.pel.portal.core.event.NotifyCreateUpdatePortalUserEvent;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@DisplayName("Testes de integração de autenticação")
class AuthenticateIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RegisterUC registerUC;

    @Autowired
    private AuthenticateUC authenticateUC;

    @Autowired
    private UserRepositoryGTW userRepositoryGTW;

    @Autowired
    private UserDependentRepositoryGTW userDependentRepositoryGTW;

    @MockitoBean
    private NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;

    @Test
    @DisplayName("Deve logar com o link de responsável")
    void shouldLoginWithResponsibleLink() {
        // Given
        // Registra o responsável
        UserRegisterDTO responsibleRegisterDTO = createValidUserRegisterDTO("adult@example.com", "11144477735");
        responsibleRegisterDTO.setBirthDate(Date.from(LocalDate.now().minusYears(25).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Mock do notifyCreateUpdatePortalUserEvent para não enviar evento
        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When
        assertDoesNotThrow(() -> registerUC.execute(responsibleRegisterDTO));

        // Then
        Optional<UserEntity> savedResponsible = userRepositoryGTW.findByCpf(responsibleRegisterDTO.getCpf());
        assertTrue(savedResponsible.isPresent());
        UserEntity responsible = savedResponsible.get();
        assertTrue(responsible.getActive());
        assertTrue(responsible.getAuthorized()); // Deve estar autorizado
        assertNotNull(responsible.getPassword());

        // Given
        // Registra o menor
        UserRegisterDTO minorRegisterDTO = createValidUserRegisterDTO("minor2@example.com", "54403121020");
        minorRegisterDTO.setBirthDate(Date.from(LocalDate.now().minusYears(16).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // When
        assertDoesNotThrow(() -> registerUC.execute(minorRegisterDTO));

        // Then
        Optional<UserEntity> savedMinor = userRepositoryGTW.findByCpf(minorRegisterDTO.getCpf());
        assertTrue(savedMinor.isPresent());
        UserEntity minor = savedMinor.get();
        assertFalse(minor.getAuthorized()); // Menor nao deve estar autorizado
        assertTrue(minor.getActive());
        assertNotNull(minor.getResponsibleToken()); // Deve ter gerado o link para o responsável

        // Given
        // Loga com o link do responsável
        AuthenticateRecord authenticateRecord = new AuthenticateRecord("11144477735", "SenhaValida123!", minor.getResponsibleToken());

        // When
        AuthenticatedRecord authenticatedRecord = authenticateUC.execute(authenticateRecord);

        // Then
        assertNotNull(authenticatedRecord.accessToken());
        Optional<UserDependentEntity> userDependent = userDependentRepositoryGTW.findByUserAndDependent(responsible, minor);
        assertTrue(userDependent.isPresent()); // Verifica se o menor foi vinculado ao responsável
    }

    @Test
    @DisplayName("Deve fazer login normal sem token")
    void shouldLoginNormallyWithoutToken() {
        // Given
        // Registra um usuário adulto (maior de 18 anos)
        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO("user@example.com", "71009062026");
        userRegisterDTO.setBirthDate(Date.from(LocalDate.now().minusYears(25).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Mock do notifyCreateUpdatePortalUserEvent para não enviar evento
        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then
        Optional<UserEntity> savedUser = userRepositoryGTW.findByCpf(userRegisterDTO.getCpf());
        assertTrue(savedUser.isPresent());
        UserEntity user = savedUser.get();
        assertTrue(user.getActive());
        assertTrue(user.getAuthorized()); // Usuário adulto deve estar autorizado automaticamente

        // Given
        // Cria o record de autenticação sem token (login normal)
        AuthenticateRecord authenticateRecord = new AuthenticateRecord(
                userRegisterDTO.getCpf(),
                userRegisterDTO.getPassword(),
                null // Sem token para login normal
        );

        // When
        AuthenticatedRecord authenticatedRecord = assertDoesNotThrow(() ->
                authenticateUC.execute(authenticateRecord)
        );

        // Then
        assertNotNull(authenticatedRecord);
        assertNotNull(authenticatedRecord.accessToken());
        assertFalse(authenticatedRecord.accessToken().isEmpty());
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
