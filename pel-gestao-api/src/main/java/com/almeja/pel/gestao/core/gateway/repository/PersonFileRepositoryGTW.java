package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonFileEntity;

import java.util.List;

public interface PersonFileRepositoryGTW {

    PersonFileEntity save(PersonFileEntity personFile);

    void delete(PersonFileEntity personFile);

    List<PersonFileEntity> findAllByPerson(PersonEntity person);

}
