package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.FileEntity;

public interface FileRepositoryGTW {

    FileEntity save(FileEntity file);

    void delete(FileEntity file);

}
