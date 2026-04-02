package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.dto.InscriptionGroupedByYearDTO;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.PersonRepositoryGTW;
import com.almeja.pel.gestao.core.mediator.Mediator;
import com.almeja.pel.gestao.core.mediator.command.GetInscriptionsGroupedByYearCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDependentInscriptionsGroupedByYearUC {

    private final PersonRepositoryGTW personRepositoryGTW;
    private final Mediator mediator;

    public List<InscriptionGroupedByYearDTO> execute(PersonEntity person, String dependentCpf) {
        PersonEntity dependent = personRepositoryGTW.findByCpf(dependentCpf).orElseThrow(() -> new ValidatorException("Dependente não encontrado."));
        if (!dependent.getResponsibleCpf().equals(person.getCpf()))
            throw new ValidatorException("Você não tem autorização para visualizar os cursos do dependente.");
        return mediator.send(new GetInscriptionsGroupedByYearCommand(dependent));
    }

}
