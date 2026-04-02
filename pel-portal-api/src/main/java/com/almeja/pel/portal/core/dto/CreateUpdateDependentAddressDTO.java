package com.almeja.pel.portal.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateUpdateDependentAddressDTO extends CreateUpdateAddressDTO {

    private boolean sameAddress;

}
