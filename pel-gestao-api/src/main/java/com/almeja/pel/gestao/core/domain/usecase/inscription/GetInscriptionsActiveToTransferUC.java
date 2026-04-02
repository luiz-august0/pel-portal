package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetInscriptionsActiveToTransferUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;

    public List<InscriptionEntity> execute(PersonEntity person) {
        return inscriptionRepositoryGTW.findAllActiveToTransferByPerson(person.getId());
    }

}
