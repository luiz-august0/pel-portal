package com.almeja.pel.portal.core.dto;

import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateAddressDTO extends BaseDTO {

    private String cep;

    private String street;

    private String number;

    private String complement;

    private String neighborhood;

    private String city;

    private String state;

}
