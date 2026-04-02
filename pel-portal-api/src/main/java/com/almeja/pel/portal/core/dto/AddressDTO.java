package com.almeja.pel.portal.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class AddressDTO extends CreateUpdateAddressDTO {

    private UUID id;

}
