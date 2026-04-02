package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.domain.enums.EnumBondType;
import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class PersonDTO extends BaseDTO {

    private Integer id;

    private String cpf;

    private String name;

    private Date birthDate;

    private String email;

    private String phone;

    private String cellphone;

    private String facebookLink;

    private EnumBondType bondType;

    private Boolean preRegistration;

    private Boolean active;

}
