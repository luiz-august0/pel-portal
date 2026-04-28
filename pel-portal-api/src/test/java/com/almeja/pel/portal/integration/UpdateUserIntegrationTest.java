package com.almeja.pel.portal.integration;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import com.almeja.pel.portal.core.domain.usecase.user.UpdateUserUC;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.dto.UserUpdateDTO;
import com.almeja.pel.portal.core.event.NotifyCreateUpdatePortalUserEvent;
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

@DisplayName("Testes de integração de edição de usuário")
class UpdateUserIntegrationTest extends BaseIntegrationTest {

    @Inject
    RegisterUC registerUC;

    @Inject
    UpdateUserUC updateUserUC;

    @Inject
    UserRepositoryGTW userRepositoryGTW;

    @InjectMock
    NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;

    @Test
    @DisplayName("Deve editar usuário adulto com sucesso após registro")
    void shouldEditAdultUserSuccessfullyAfterRegistration() {
        // Given
        String originalCpf = "11144477735";
        String originalEmail = "usuario.original@example.com";
        String originalName = "Usuario Original";
        String originalPhone = "11987654321";

        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO(originalEmail, originalCpf);
        userRegisterDTO.setName(originalName);
        userRegisterDTO.setPhone(originalPhone);
        userRegisterDTO.setBirthDate(Date.from(LocalDate.now().minusYears(25).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then
        Optional<UserEntity> registeredUser = userRepositoryGTW.findByCpf(originalCpf);
        assertTrue(registeredUser.isPresent());
        UserEntity user = registeredUser.get();
        assertTrue(user.getActive());
        assertTrue(user.getAuthorized());
        assertEquals(originalName, user.getName());
        assertEquals(originalEmail, user.getEmail());
        assertEquals(originalPhone, user.getUserDetails().getPhone());

        // Given
        String newName = "Usuario Editado";
        String newEmail = "usuario.editado@example.com";
        String newPhone = "11999888777";
        Date newBirthDate = Date.from(LocalDate.now().minusYears(30).atStartOfDay(ZoneId.systemDefault()).toInstant());

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setName(newName);
        userUpdateDTO.setEmail(newEmail);
        userUpdateDTO.setPhone(newPhone);
        userUpdateDTO.setBirthDate(newBirthDate);
        userUpdateDTO.setCpf(originalCpf);

        // When
        assertDoesNotThrow(() -> updateUserUC.execute(user, userUpdateDTO));

        // Then
        Optional<UserEntity> editedUser = userRepositoryGTW.findByCpf(originalCpf);
        assertTrue(editedUser.isPresent());
        UserEntity updatedUser = editedUser.get();

        assertEquals(newName, updatedUser.getName());
        assertEquals(newEmail, updatedUser.getEmail());
        assertEquals(newPhone, updatedUser.getUserDetails().getPhone());
        assertEquals(originalCpf, updatedUser.getCpf());

        assertTrue(updatedUser.getActive());
        assertTrue(updatedUser.getAuthorized());
        assertEquals(user.getId(), updatedUser.getId());
        assertNotNull(updatedUser.getUpdatedAt());
    }

    private UserRegisterDTO createValidUserRegisterDTO(String email, String cpf) {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setName("Usuario de Teste");
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
