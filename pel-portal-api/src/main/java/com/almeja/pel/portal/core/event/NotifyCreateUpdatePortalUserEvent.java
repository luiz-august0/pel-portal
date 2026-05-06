package com.almeja.pel.portal.core.event;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.AddressDTO;
import com.almeja.pel.portal.core.dto.DocumentDTO;
import com.almeja.pel.portal.core.dto.UserDetailsDTO;
import com.almeja.pel.portal.core.event.dto.EventPortalUserDTO;
import com.almeja.pel.portal.core.gateway.event.EventProducerGTW;
import com.almeja.pel.portal.core.repository.DocumentRepository;
import com.almeja.pel.portal.core.repository.UserDependentRepository;
import com.almeja.pel.portal.core.util.ConverterEntityToDTOUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class NotifyCreateUpdatePortalUserEvent {

    private final EventProducerGTW eventProducerGTW;
    private final DocumentRepository documentRepository;
    private final UserDependentRepository userDependentRepository;

    public void send(UserEntity user) {
        EventPortalUserDTO eventPortalUser = createEventPortalUser(user);
        if (user.getUserDetails().isMinor()) {
            Optional<UserDependentEntity> responsible = userDependentRepository.findByDependent(user);
            responsible.ifPresent(dependent -> eventPortalUser.setResponsible(createEventPortalUser(dependent.getUser())));
        }
        eventProducerGTW.send("portal-update-create-user", eventPortalUser);
    }

    private EventPortalUserDTO createEventPortalUser(UserEntity user) {
        List<DocumentEntity> documents = documentRepository.findAllByUser(user);
        return EventPortalUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .cpf(user.getCpf())
                .documents(ConverterEntityToDTOUtil.convert(documents, DocumentDTO.class))
                .address(ConverterEntityToDTOUtil.convert(user.getAddress(), AddressDTO.class))
                .userDetails(ConverterEntityToDTOUtil.convert(user.getUserDetails(), UserDetailsDTO.class))
                .build();
    }

}
