package com.almeja.pel.portal.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DependentCreateDTO extends UserUpdateDTO {

    private String password;

}
