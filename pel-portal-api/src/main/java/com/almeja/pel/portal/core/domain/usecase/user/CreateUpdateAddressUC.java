package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.AddressEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.CreateUpdateAddressDTO;
import com.almeja.pel.portal.core.mediator.Mediator;
import com.almeja.pel.portal.core.mediator.command.UpdateUserCommand;
import com.almeja.pel.portal.core.repository.AddressRepository;
import com.almeja.pel.portal.core.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class CreateUpdateAddressUC {

    @Inject
    AddressRepository addressRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    Mediator mediator;

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
        AddressEntity addressSaved = addressRepository.save(address);
        userRepository.save(user);
        mediator.send(new UpdateUserCommand(user));
        return addressSaved.getId();
    }

}
