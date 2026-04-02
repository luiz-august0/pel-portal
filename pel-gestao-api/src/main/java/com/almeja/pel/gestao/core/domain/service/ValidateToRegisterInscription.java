package com.almeja.pel.gestao.core.domain.service;

import com.almeja.pel.gestao.core.domain.entity.ClassEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.exception.ValidatorException;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateToRegisterInscription {

    private final ClassRepositoryGTW classRepositoryGTW;
    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;

    public void validate(PersonEntity person, ClassEntity clazz) {
        // Valida se usuário foi revisado
        if (person.isNotReviewed()) throw new ValidatorException("Você não pode realizar inscrição.");
        Integer subscribers = classRepositoryGTW.getSubscribersByClass(clazz.getId());
        // Valida vagas disponiveis
        if (subscribers >= clazz.getAvailableSlots()) {
            throw new ValidatorException("Turma lotada. não é possível realizar inscrição.");
        }
        // Valida idade do aluno para a turma
        if (!clazz.isPersonAgePermitted(person)) {
            throw new ValidatorException("Idade não permite inscrição nesta turma.");
        }
        // Valida se a turma esta inativa
        if (Boolean.TRUE.equals(clazz.getInactive())) {
            throw new ValidatorException("Turma inativa. não é possível realizar inscrição.");
        }
        // Valida se o aluno ja possui matricula ativa para o curso
        if (!inscriptionRepositoryGTW.findAllByPersonAndCourseAndStatusActiveAndCurrentYear(person, clazz.getCourse()).isEmpty()) {
            throw new ValidatorException("Você já possui matricula ativa para este curso.");
        }
        // Valida se o aluno ja possui matricula ativa que se sobrepoe com a nova
        if (!inscriptionRepositoryGTW.findAllOverlapsByPersonAndClassAndStatusActiveAndCurrentYear(person.getId(), clazz.getId()).isEmpty()) {
            throw new ValidatorException("Você já possui matricula ativa que se sobrepoe a esta.");
        }
    }

}
