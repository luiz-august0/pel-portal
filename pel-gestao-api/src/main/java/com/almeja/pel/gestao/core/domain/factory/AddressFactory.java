package com.almeja.pel.gestao.core.domain.factory;

import com.almeja.pel.gestao.core.domain.entity.AddressEntity;
import org.springframework.stereotype.Service;

@Service
public class AddressFactory {

    public AddressEntity createAddress(String street, String number, String neighborhood, String city, String state, String zipCode, String complement) {
        return AddressEntity.builder()
                .street(street)
                .number(number)
                .neighborhood(neighborhood)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .complement(complement)
                .build();
    }

}
