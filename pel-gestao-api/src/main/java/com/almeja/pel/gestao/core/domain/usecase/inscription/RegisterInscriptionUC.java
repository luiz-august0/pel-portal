package com.almeja.pel.gestao.core.domain.usecase.inscription;

import com.almeja.pel.gestao.core.domain.entity.ClassEntity;
import com.almeja.pel.gestao.core.domain.entity.InscriptionEntity;
import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.factory.InscriptionFactory;
import com.almeja.pel.gestao.core.domain.service.ValidateToRegisterInscription;
import com.almeja.pel.gestao.core.gateway.repository.ClassRepositoryGTW;
import com.almeja.pel.gestao.core.gateway.repository.InscriptionRepositoryGTW;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterInscriptionUC {

    private final InscriptionRepositoryGTW inscriptionRepositoryGTW;
    private final ClassRepositoryGTW classRepositoryGTW;
    private final InscriptionFactory inscriptionFactory;
    private final ValidateToRegisterInscription validateToRegisterInscription;

    @Transactional
    public Integer execute(PersonEntity person, Integer classId) {
        ClassEntity clazz = classRepositoryGTW.findAndValidate(classId);
        validateToRegisterInscription.validate(person, clazz);
        InscriptionEntity inscription = InscriptionEntity.create(person, clazz);
        inscriptionFactory.fillContract(inscription);
        return inscriptionRepositoryGTW.save(inscription).getId();
    }

}
