package com.almeja.pel.gestao.infra.repository;

import com.almeja.pel.gestao.core.domain.entity.FileEntity;
import com.almeja.pel.gestao.core.gateway.repository.FileRepositoryGTW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer>, FileRepositoryGTW {

}
