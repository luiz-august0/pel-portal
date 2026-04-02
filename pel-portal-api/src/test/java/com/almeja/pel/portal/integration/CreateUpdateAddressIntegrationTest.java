package com.almeja.pel.portal.integration;

import com.almeja.pel.portal.core.domain.entity.AddressEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.domain.usecase.user.CreateUpdateAddressUC;
import com.almeja.pel.portal.core.domain.usecase.user.RegisterUC;
import com.almeja.pel.portal.core.dto.CreateUpdateAddressDTO;
import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.event.NotifyCreateUpdatePortalUserEvent;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@DisplayName("Testes de integração de criação e edição de endereço")
class CreateUpdateAddressIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RegisterUC registerUC;

    @Autowired
    private CreateUpdateAddressUC createUpdateAddressUC;

    @Autowired
    private UserRepositoryGTW userRepositoryGTW;

    @MockitoBean
    private NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;

    @Test
    @DisplayName("Deve criar endereço com sucesso para usuário sem endereço")
    void shouldCreateAddressSuccessfullyWhenUserHasNoAddress() {
        // Given - Registrar usuário adulto sem endereço
        String cpf = "10452376041";
        String email = "usuario.teste@example.com";

        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO(email, cpf);

        // Mock do notifyCreateUpdatePortalUserEvent para não enviar evento
        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        // When - Registrar usuário
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        // Then - Verificar se usuário foi registrado sem endereço
        Optional<UserEntity> registeredUser = userRepositoryGTW.findByCpf(cpf);
        assertThat(registeredUser).isPresent();
        UserEntity user = registeredUser.get();
        assertThat(user.getAddress()).isNull(); // Usuário não deve ter endereço inicialmente

        // Given - Dados do endereço a ser criado
        CreateUpdateAddressDTO addressDTO = createValidCreateUpdateAddressDTO();

        // When - Criar endereço
        assertDoesNotThrow(() -> createUpdateAddressUC.execute(user, addressDTO));

        // Then - Verificar se endereço foi criado
        Optional<UserEntity> userWithAddress = userRepositoryGTW.findById(user.getId());
        assertThat(userWithAddress).isPresent();
        UserEntity updatedUser = userWithAddress.get();

        // Verificar dados do endereço criado
        assertThat(updatedUser.getAddress()).isNotNull();
        AddressEntity address = updatedUser.getAddress();
        assertThat(address.getCep()).isEqualTo("12345678");
        assertThat(address.getStreet()).isEqualTo("Rua das Flores");
        assertThat(address.getNumber()).isEqualTo("123");
        assertThat(address.getComplement()).isEqualTo("Apto 45");
        assertThat(address.getNeighborhood()).isEqualTo("Centro");
        assertThat(address.getCity()).isEqualTo("São Paulo");
        assertThat(address.getState()).isEqualTo("SP");
        assertThat(address.getId()).isNotNull();
        assertThat(address.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve editar endereço existente com sucesso")
    void shouldUpdateExistingAddressSuccessfully() {
        // Given - Registrar usuário e criar endereço inicial
        String cpf = "29395428058";
        String email = "usuario.edicao@example.com";

        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO(email, cpf);
        
        // Mock do notifyCreateUpdatePortalUserEvent para não enviar evento
        doNothing().when(notifyCreateUpdatePortalUserEvent).send(any());

        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        Optional<UserEntity> registeredUser = userRepositoryGTW.findByCpf(cpf);
        assertThat(registeredUser).isPresent();
        UserEntity user = registeredUser.get();

        // Criar endereço inicial
        CreateUpdateAddressDTO initialAddressDTO = createValidCreateUpdateAddressDTO();
        assertDoesNotThrow(() -> createUpdateAddressUC.execute(user, initialAddressDTO));

        // Verificar endereço inicial foi criado
        Optional<UserEntity> userWithInitialAddress = userRepositoryGTW.findById(user.getId());
        assertThat(userWithInitialAddress).isPresent();
        UserEntity userWithAddress = userWithInitialAddress.get();
        assertThat(userWithAddress.getAddress()).isNotNull();
        AddressEntity initialAddress = userWithAddress.getAddress();

        // Given - Dados do endereço editado
        CreateUpdateAddressDTO updatedAddressDTO = new CreateUpdateAddressDTO();
        updatedAddressDTO.setCep("87654321");
        updatedAddressDTO.setStreet("Avenida Paulista");
        updatedAddressDTO.setNumber("1000");
        updatedAddressDTO.setComplement("Conjunto 200");
        updatedAddressDTO.setNeighborhood("Bela Vista");
        updatedAddressDTO.setCity("São Paulo");
        updatedAddressDTO.setState("SP");

        // When - Editar endereço
        assertDoesNotThrow(() -> createUpdateAddressUC.execute(userWithAddress, updatedAddressDTO));

        // Then - Verificar se endereço foi editado
        Optional<UserEntity> userWithUpdatedAddress = userRepositoryGTW.findById(user.getId());
        assertThat(userWithUpdatedAddress).isPresent();
        UserEntity finalUser = userWithUpdatedAddress.get();

        // Verificar dados do endereço editado
        assertThat(finalUser.getAddress()).isNotNull();
        AddressEntity updatedAddress = finalUser.getAddress();

        // Verificar que é o mesmo endereço (mesmo ID) mas com dados atualizados
        assertThat(updatedAddress.getId()).isEqualTo(initialAddress.getId());
        assertThat(updatedAddress.getCep()).isEqualTo("87654321");
        assertThat(updatedAddress.getStreet()).isEqualTo("Avenida Paulista");
        assertThat(updatedAddress.getNumber()).isEqualTo("1000");
        assertThat(updatedAddress.getComplement()).isEqualTo("Conjunto 200");
        assertThat(updatedAddress.getNeighborhood()).isEqualTo("Bela Vista");
        assertThat(updatedAddress.getCity()).isEqualTo("São Paulo");
        assertThat(updatedAddress.getState()).isEqualTo("SP");

        // Verificar que timestamps foram atualizados
        assertThat(updatedAddress.getCreatedAt()).isEqualTo(initialAddress.getCreatedAt());
        assertThat(updatedAddress.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve criar e depois editar endereço em sequência")
    void shouldCreateAndThenUpdateAddressInSequence() {
        // Given - Registrar usuário
        String cpf = "90916419088";
        String email = "usuario.sequencia@example.com";

        UserRegisterDTO userRegisterDTO = createValidUserRegisterDTO(email, cpf);
        assertDoesNotThrow(() -> registerUC.execute(userRegisterDTO));

        Optional<UserEntity> registeredUser = userRepositoryGTW.findByCpf(cpf);
        assertThat(registeredUser).isPresent();
        UserEntity user = registeredUser.get();
        assertThat(user.getAddress()).isNull();

        // When - Criar endereço
        CreateUpdateAddressDTO createAddressDTO = createValidCreateUpdateAddressDTO();
        assertDoesNotThrow(() -> createUpdateAddressUC.execute(user, createAddressDTO));

        // Then - Verificar criação
        Optional<UserEntity> userAfterCreation = userRepositoryGTW.findById(user.getId());
        assertThat(userAfterCreation).isPresent();
        UserEntity userWithAddress = userAfterCreation.get();
        assertThat(userWithAddress.getAddress()).isNotNull();

        AddressEntity createdAddress = userWithAddress.getAddress();
        assertThat(createdAddress.getCep()).isEqualTo("12345678");
        assertThat(createdAddress.getStreet()).isEqualTo("Rua das Flores");

        // When - Editar o mesmo endereço
        CreateUpdateAddressDTO updateAddressDTO = new CreateUpdateAddressDTO();
        updateAddressDTO.setCep("99988877");
        updateAddressDTO.setStreet("Rua das Palmeiras");
        updateAddressDTO.setNumber("456");
        updateAddressDTO.setComplement("Casa 2");
        updateAddressDTO.setNeighborhood("Jardim");
        updateAddressDTO.setCity("Rio de Janeiro");
        updateAddressDTO.setState("RJ");

        assertDoesNotThrow(() -> createUpdateAddressUC.execute(userWithAddress, updateAddressDTO));

        // Then - Verificar edição
        Optional<UserEntity> userAfterUpdate = userRepositoryGTW.findById(user.getId());
        assertThat(userAfterUpdate).isPresent();
        UserEntity finalUser = userAfterUpdate.get();

        AddressEntity updatedAddress = finalUser.getAddress();

        // Verificar que é o mesmo endereço editado
        assertThat(updatedAddress.getId()).isEqualTo(createdAddress.getId());
        assertThat(updatedAddress.getCep()).isEqualTo("99988877");
        assertThat(updatedAddress.getStreet()).isEqualTo("Rua das Palmeiras");
        assertThat(updatedAddress.getNumber()).isEqualTo("456");
        assertThat(updatedAddress.getComplement()).isEqualTo("Casa 2");
        assertThat(updatedAddress.getNeighborhood()).isEqualTo("Jardim");
        assertThat(updatedAddress.getCity()).isEqualTo("Rio de Janeiro");
        assertThat(updatedAddress.getState()).isEqualTo("RJ");
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

    private CreateUpdateAddressDTO createValidCreateUpdateAddressDTO() {
        CreateUpdateAddressDTO dto = new CreateUpdateAddressDTO();
        dto.setCep("12345678");
        dto.setStreet("Rua das Flores");
        dto.setNumber("123");
        dto.setComplement("Apto 45");
        dto.setNeighborhood("Centro");
        dto.setCity("São Paulo");
        dto.setState("SP");
        return dto;
    }

}
