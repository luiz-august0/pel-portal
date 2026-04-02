package com.almeja.pel.portal.core.event;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.dto.AddressDTO;
import com.almeja.pel.portal.core.dto.DocumentDTO;
import com.almeja.pel.portal.core.dto.UserDetailsDTO;
import com.almeja.pel.portal.core.event.dto.EventPortalUserDTO;
import com.almeja.pel.portal.core.gateway.event.EventProducerGTW;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import com.almeja.pel.portal.core.util.ConverterEntityToDTOUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotifyCreateUpdatePortalUserEvent {

    private final EventProducerGTW eventProducerGTW;
    private final DocumentRepositoryGTW documentRepositoryGTW;
    private final UserDependentRepositoryGTW userDependentRepositoryGTW;

    @Value("${spring.kafka.topics.portal-update-create-user}")
    private String eventName;

    public void send(UserEntity user) {
        EventPortalUserDTO eventPortalUser = createEventPortalUser(user);
        if (user.getUserDetails().isMinor()) {
            Optional<UserDependentEntity> responsible = userDependentRepositoryGTW.findByDependent(user);
            responsible.ifPresent(dependent -> eventPortalUser.setResponsible(createEventPortalUser(dependent.getUser())));
        }
        eventProducerGTW.send(eventName, eventPortalUser);
    }

    private EventPortalUserDTO createEventPortalUser(UserEntity user) {
        List<DocumentEntity> documents = documentRepositoryGTW.findAllByUser(user);
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
