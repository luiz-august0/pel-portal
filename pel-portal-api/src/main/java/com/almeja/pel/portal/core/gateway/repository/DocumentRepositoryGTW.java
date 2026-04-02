package com.almeja.pel.portal.core.gateway.repository;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;

import java.util.List;
import java.util.Optional;

public interface DocumentRepositoryGTW {

    DocumentEntity save(DocumentEntity document);

    void delete(DocumentEntity document);

    Optional<DocumentEntity> findByUserAndDocumentType(UserEntity user, EnumDocumentType documentType);

    List<DocumentEntity> findAllByUser(UserEntity user);

}
