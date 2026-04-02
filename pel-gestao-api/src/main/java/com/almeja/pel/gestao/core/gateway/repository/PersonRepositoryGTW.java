package com.almeja.pel.gestao.core.gateway.repository;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;

import java.util.List;
import java.util.Optional;

public interface PersonRepositoryGTW {

    PersonEntity save(PersonEntity person);

    Optional<PersonEntity> findByCpf(String cpf);

    List<PersonEntity> findAllByResponsibleCpf(String cpf);

}
