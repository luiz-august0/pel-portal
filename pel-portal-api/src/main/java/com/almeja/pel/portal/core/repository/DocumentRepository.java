package com.almeja.pel.portal.core.repository;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {

    void delete(DocumentEntity document);

    Optional<DocumentEntity> findByUserAndDocumentType(UserEntity user, EnumDocumentType documentType);

    List<DocumentEntity> findAllByUser(UserEntity user);

}
