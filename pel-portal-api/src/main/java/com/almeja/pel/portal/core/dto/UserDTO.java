package com.almeja.pel.portal.core.dto;

import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class UserDTO extends BaseDTO {

    private UUID id;

    private String name;

    private String email;

    private String cpf;

    private Boolean active;

    private Boolean authorized;

    private Boolean reviewed;

    private String responsibleToken;

    private Date responsibleTokenGeneratedAt;

    private Date responsibleTokenExpiresAt;

    private UserDetailsDTO userDetails;

    private AddressDTO address;

}
