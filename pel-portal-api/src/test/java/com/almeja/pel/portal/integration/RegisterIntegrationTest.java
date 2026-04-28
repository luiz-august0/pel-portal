package com.almeja.pel.portal.integration;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.domain.usecase.dependent.ListDependentsLinkedUC;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import com.almeja.pel.portal.core.dto.DependentsLinkedListDTO;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.event.NotifyCreateUpdatePortalUserEvent;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
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

@DisplayName("Testes de integração de registro de usuário")
class RegisterIntegrationTest extends BaseIntegrationTest {

    @Inject
    RegisterUC registerUC;

    @Inject
    UserRepositoryGTW userRepositoryGTW;

    @Inject
    UserDependentRepositoryGTW userDependentRepositoryGTW;

    @Inject
    ListDependentsLinkedUC listDependentsLinkedUC;

    @InjectMock
    NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;

    @Test
    @DisplayName("Deve registrar um usuário adulto com sucesso")
    void shouldRegisterAdultUserSuccessfully() {
        // Given
        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO("adult@example.com", "11144477735");
        userRegisterDTO.setBirthDate(Date.from(LocalDate.now().minusYears(25).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());
        // When
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then
        Optional<UserEntity> savedUser = userRepositoryGTW.findByCpf(userRegisterDTO.getCpf());
        assertTrue(savedUser.isPresent());
        UserEntity user = savedUser.get();
        assertTrue(user.getActive());
        assertTrue(user.getAuthorized());
        assertNotNull(user.getPassword());
    }

    @Test
    @DisplayName("Deve registrar um usuário menor como nao autorizado")
    void shouldRegisterMinorUserAsUnauthorized() {
        // Given
        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO("minor@example.com", "22255588846");
        userRegisterDTO.setBirthDate(Date.from(LocalDate.now().minusYears(16).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then
        Optional<UserEntity> savedUser = userRepositoryGTW.findByCpf(userRegisterDTO.getCpf());
        assertTrue(savedUser.isPresent());
        UserEntity user = savedUser.get();
        assertFalse(user.getAuthorized());
        assertTrue(user.getActive());
        assertNotNull(user.getResponsibleToken());
    }

    @Test
    @DisplayName("Deve registrar um usuário responsavel com link enviado pelo usúario menor")
    void shouldRegisterResponsibleUser() {
        // Given
        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO("minor2@example.com", "54403121020");
        userRegisterDTO.setBirthDate(Date.from(LocalDate.now().minusYears(16).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then
        Optional<UserEntity> savedUser = userRepositoryGTW.findByCpf(userRegisterDTO.getCpf());
        assertTrue(savedUser.isPresent());
        UserEntity minor = savedUser.get();
        assertFalse(minor.getAuthorized());
        assertTrue(minor.getActive());
        assertNotNull(minor.getResponsibleToken());

        // Given
        UserRegisterDTO DTOResponsible = createValidUserRegisterDTO("adult2@example.com", "06236654093");
        DTOResponsible.setBirthDate(Date.from(LocalDate.now().minusYears(25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        DTOResponsible.setAuthorizedToken(minor.getResponsibleToken());

        // When
        assertDoesNotThrow(() -> registerUC.execute(DTOResponsible));

        // Then
        Optional<UserEntity> savedUserResponsible = userRepositoryGTW.findByCpf(DTOResponsible.getCpf());
        assertTrue(savedUserResponsible.isPresent());
        UserEntity userResponsible = savedUserResponsible.get();
        assertTrue(userResponsible.getActive());
        assertTrue(userResponsible.getAuthorized());
        Optional<UserDependentEntity> userDependent = userDependentRepositoryGTW.findByUserAndDependent(userResponsible, minor);
        assertTrue(userDependent.isPresent());

        // When
        DependentsLinkedListDTO dependents = listDependentsLinkedUC.execute(userResponsible);

        // Then
        assertEquals(1, dependents.getPending().size());
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
