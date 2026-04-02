package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.AddressEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.CreateUpdateAddressDTO;
import com.almeja.pel.portal.core.gateway.repository.AddressRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserRepositoryGTW;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateUpdateAddressUC {

    private final AddressRepositoryGTW addressRepositoryGTW;
    private final UserRepositoryGTW userRepositoryGTW;
    private final Mediator mediator;

    @Transactional
    public UUID execute(UserEntity user, CreateUpdateAddressDTO createUpdateAddressDTO) {
        AddressEntity address;
        // Atualizar endereço existente
        if (user.getAddress() != null) {
            address = user.getAddress();
            address.updateAddress(createUpdateAddressDTO.getCep(), createUpdateAddressDTO.getStreet(), createUpdateAddressDTO.getNumber(),
                    createUpdateAddressDTO.getComplement(), createUpdateAddressDTO.getNeighborhood(), createUpdateAddressDTO.getCity(),
                    createUpdateAddressDTO.getState());
        } else {
            // Cria novo endereço
            address = new AddressEntity(createUpdateAddressDTO.getCep(), createUpdateAddressDTO.getStreet(), createUpdateAddressDTO.getNumber(),
                    createUpdateAddressDTO.getComplement(), createUpdateAddressDTO.getNeighborhood(), createUpdateAddressDTO.getCity(),
                    createUpdateAddressDTO.getState()
            );
            // Vincula endereço ao usuário
            user.updateAddress(address);
        }
        // Salva endereço e usuário
        AddressEntity addressSaved = addressRepositoryGTW.save(address);
        userRepositoryGTW.save(user);
        mediator.send(new UpdateUserCommand(user));
        return addressSaved.getId();
    }

}
