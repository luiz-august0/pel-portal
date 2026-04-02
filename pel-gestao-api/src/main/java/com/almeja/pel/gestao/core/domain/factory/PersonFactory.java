package com.almeja.pel.gestao.core.domain.factory;

import com.almeja.pel.gestao.core.domain.entity.PersonEntity;
import com.almeja.pel.gestao.core.domain.enums.EnumBondType;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonFactory {

    public PersonEntity createPerson(String cpf, String name, Date birthDate, String email, String phone, String origin,
                                     EnumBondType bondType, boolean preRegistration) {
        return PersonEntity.builder()
                .cpf(cpf)
                .name(name)
                .birthDate(birthDate)
                .email(email)
                .cellphone(phone)
                .bondType(bondType)
                .preRegistration(preRegistration)
                .origin(origin)
                .build();
    }

}
