package com.almeja.pel.gestao.core.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.core.dto.enums.EnumPortalInternalRelationshipType;
import com.almeja.pel.gestao.core.dto.enums.EnumPortalProgramKnowledgeSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PortalUserDetailsDTO extends BaseDTO {

    private Date birthDate;

    private String phone;

    private Boolean specialNeeds;

    private EnumPortalProgramKnowledgeSource programKnowledgeSource;

    private String programKnowledgeSourceOther;

    private EnumPortalInternalRelationshipType internalRelationshipType;

    private boolean isMinor;

}
