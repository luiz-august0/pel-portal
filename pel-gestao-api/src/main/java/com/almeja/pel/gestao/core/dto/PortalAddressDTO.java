package com.almeja.pel.gestao.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class PortalAddressDTO {

    private UUID id;

    private String cep;

    private String street;

    private String number;

    private String complement;

    private String neighborhood;

    private String city;

    private String state;

}
