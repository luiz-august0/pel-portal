package com.almeja.pel.portal.core.domain.usecase.user;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.event.NotifyCreateUpdatePortalUserEvent;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import com.almeja.pel.portal.core.gateway.repository.UserDependentRepositoryGTW;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;

@ApplicationScoped
public class VerifyAndNotifyCreateUpdateUserUC {

    @Inject
    NotifyCreateUpdatePortalUserEvent notifyCreateUpdatePortalUserEvent;
    @Inject
    DocumentRepositoryGTW documentRepositoryGTW;
    @Inject
    UserDependentRepositoryGTW userDependentRepositoryGTW;

    @Transactional
    public void execute(UserEntity user) {
        verifyAndNotify(user);
        if (!user.getUserDetails().isMinor()) {
            List<UserDependentEntity> dependents = userDependentRepositoryGTW.findAllByUser(user);
            dependents.forEach((dependent) -> verifyAndNotify(dependent.getDependent()));
        }
    }

    private void verifyAndNotify(UserEntity user) {
        List<EnumDocumentType> documents = documentRepositoryGTW.findAllByUser(user)
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
