package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.PersonFileEntity;
import com.almeja.pel.gestao.core.gateway.repository.PersonFileRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonFileRepository extends JpaRepository<PersonFileEntity, Integer>, PersonFileRepositoryGTW {

    List<PersonFileEntity> findByPersonId(Integer personId);
    
    List<PersonFileEntity> findByFileId(Integer fileId);

}
