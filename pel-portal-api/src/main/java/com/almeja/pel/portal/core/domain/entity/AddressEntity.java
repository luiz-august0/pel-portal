package com.almeja.pel.portal.core.domain.entity;

import com.almeja.pel.portal.core.domain.entity.base.BaseEntity;
import com.almeja.pel.portal.core.domain.service.AddressValidatorService;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "portal_address")
public class AddressEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "cep", length = 8, nullable = false)
    private String cep;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", length = 20, nullable = false)
    private String number;

    @Column(name = "complement", length = 100)
    private String complement;

    @Column(name = "neighborhood", length = 100, nullable = false)
    private String neighborhood;

    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @Column(name = "state", length = 2, nullable = false)
    private String state;

    public AddressEntity(String cep, String street, String number, String complement, 
                        String neighborhood, String city, String state) {
        atributeValuesAndValidate(cep, street, number, complement, neighborhood, city, state);
    }

    public void updateAddress(String cep, String street, String number, String complement, 
                             String neighborhood, String city, String state) {
        atributeValuesAndValidate(cep, street, number, complement, neighborhood, city, state);
    }

    private void atributeValuesAndValidate(String cep, String street, String number, String complement, String neighborhood, String city, String state) {
        AddressValidatorService.validateCep(cep);
        AddressValidatorService.validateStreet(street);
        AddressValidatorService.validateNumber(number);
        AddressValidatorService.validateComplement(complement);
        AddressValidatorService.validateNeighborhood(neighborhood);
        AddressValidatorService.validateCity(city);
        AddressValidatorService.validateState(state);
        this.cep = cep.trim().replaceAll("[^0-9]", "");
        this.street = street.trim();
        this.number = number.trim();
        this.complement = complement.trim();
        this.neighborhood = neighborhood.trim();
        this.city = city.trim();
        this.state = state.trim().toUpperCase();
    }

}
