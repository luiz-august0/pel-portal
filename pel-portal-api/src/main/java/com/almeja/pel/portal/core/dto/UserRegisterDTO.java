package com.almeja.pel.portal.core.dto;

import com.almeja.pel.portal.core.dto.base.BaseDTO;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "email", callSuper = false)
public class UserRegisterDTO extends BaseDTO {

    private String name;

    private String email;

    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private Date birthDate;

    private String phone;

    private String cpf;

    private Boolean specialNeeds;

    private EnumProgramKnowledgeSource programKnowledgeSource;

    private String programKnowledgeSourceOther;

    private String authorizedToken;

}
