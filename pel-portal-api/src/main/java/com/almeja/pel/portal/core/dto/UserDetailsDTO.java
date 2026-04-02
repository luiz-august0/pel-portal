package com.almeja.pel.portal.core.dto;

import com.almeja.pel.portal.core.domain.enums.EnumInternalRelationshipType;
import com.almeja.pel.portal.core.domain.enums.EnumProgramKnowledgeSource;
import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDetailsDTO extends BaseDTO {

    private Date birthDate;

    private String phone;

    private Boolean specialNeeds;

    private EnumProgramKnowledgeSource programKnowledgeSource;

    private String programKnowledgeSourceOther;

    private EnumInternalRelationshipType internalRelationshipType;

    private boolean isMinor;

}
