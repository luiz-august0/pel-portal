package com.almeja.pel.gestao.core.domain.service;

import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.PersonRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateInscriptionOwnerService {

    private final PersonRepositoryGTW personRepositoryGTW;
    private static final String ERROR_MESSAGE = "Você não tem permissão para realizar ações sobre esta inscrição.";

    public void validateOwner(PersonEntity person, InscriptionEntity inscription) {
        // Verifica se é o dono da inscrição
        boolean isOwner = person.getId().equals(inscription.getStudent().getId());
        // Se nao for menor e nao for o dono da inscricao, verifica se há dependentes do responsável que seja dono da inscricao
        if (!person.isMinor() && !isOwner) {
            boolean anyDependentHasInscription = personRepositoryGTW.findAllByResponsibleCpf(person.getCpf()).stream().anyMatch(
                    dependent -> inscription.getStudent().getCpf().equals(dependent.getCpf()));
            if (!anyDependentHasInscription) throw new ValidatorException(ERROR_MESSAGE);
        }
        if (!isOwner) throw new ValidatorException(ERROR_MESSAGE);
    }

}
