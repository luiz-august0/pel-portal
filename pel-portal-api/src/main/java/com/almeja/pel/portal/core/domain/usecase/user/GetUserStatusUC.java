package com.almeja.pel.portal.core.domain.usecase.user;

import jakarta.inject.Inject;
import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.dto.UserStatusDTO;
import com.almeja.pel.portal.core.repository.DocumentRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GetUserStatusUC {

    @Inject
    DocumentRepository documentRepository;

    public List<UserStatusDTO> execute(UserEntity user) {
        List<UserStatusDTO> status = new ArrayList<>();
        List<DocumentEntity> documents = documentRepository.findAllByUser(user);
        if (user.getUserDetails().isMinor())
            status.add(new UserStatusDTO("Link do responsável.", user.getAuthorized()));
        status.add(new UserStatusDTO("Cadastro do endereço.", user.getAddress() != null));
        status.add(new UserStatusDTO("Upload do documento com foto.", hasDocument(documents, EnumDocumentType.DOCUMENT_WITH_PHOTO)));
        if (Boolean.TRUE.equals(user.getUserDetails().getSpecialNeeds())) {
            status.add(new UserStatusDTO("Upload do laudo médico.", hasDocument(documents, EnumDocumentType.MEDICAL_REPORT)));
        }
        status.add(new UserStatusDTO("Upload do comprovante de vínculo interno (opcional).", hasDocument(documents, EnumDocumentType.PROOF_OF_INTERNAL_RELATIONSHIP), true));
        return status;
    }

    private boolean hasDocument(List<DocumentEntity> documents, EnumDocumentType documentType) {
        return documents.stream().anyMatch(doc -> doc.getDocumentType().equals(documentType));
    }

}