package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetLastInscriptionUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;

    public InscriptionEntity execute(PersonEntity person) {
        return inscriptionRepositoryGTW.findLastByPerson(person.getId());
    }
    
}
