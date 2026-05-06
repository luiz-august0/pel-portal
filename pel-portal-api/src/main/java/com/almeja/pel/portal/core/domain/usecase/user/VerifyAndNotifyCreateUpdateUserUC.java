package com.almeja.pel.portal.core.domain.usecase.user;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.event.NotifyCreateUpdatePortalUserEvent;
import com.almeja.pel.portal.core.repository.DocumentRepository;
import com.almeja.pel.portal.core.repository.UserDependentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class VerifyAndNotifyCreateUpdateUserUC {

    private final NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;
    private final DocumentRepository documentRepository;
    private final UserDependentRepository userDependentRepository;

    @Transactional
    public void execute(UserEntity user) {
        verifyAndNotify(user);
        if (!user.getUserDetails().isMinor()) {
            List<UserDependentEntity> dependents = userDependentRepository.findAllByUser(user);
            dependents.forEach((dependent) -> verifyAndNotify(dependent.getDependent()));
        }
    }

    private void verifyAndNotify(UserEntity user) {
        List<EnumDocumentType> documents = documentRepository.findAllByUser(user)
                .stream()
                .map(DocumentEntity::getDocumentType)
                .toList();
        // Se for menor e não tiver autorizado, não notificar
        if (user.getUserDetails().isMinor() && !user.getAuthorized()) return;
        // Se nao tiver todos os documentos obrigatórios, não notificar
        if (!new HashSet<>(documents).containsAll(EnumDocumentType.getRequiredDocuments(user.getUserDetails()))) return;
        // Se nao tiver endereco cadastrado, não notificar
        if (user.getAddress() == null) return;
        notifyCreateUpdatePortalUserEvent.send(user);
    }

}
