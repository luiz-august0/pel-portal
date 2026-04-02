package com.almeja.pel.portal.infra.repository;

import com.almeja.pel.portal.core.domain.entity.DocumentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;
import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.gateway.repository.DocumentRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID>, DocumentRepositoryGTW {

    List<DocumentEntity> findAllByUser(UserEntity user);

}
