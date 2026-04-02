package com.almeja.pel.portal.core.dto;

import com.almeja.pel.portal.core.domain.enums.EnumDependentRelationship;
import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DependentRelationshipAndSpecialNeedsDTO extends BaseDTO {

    private EnumDependentRelationship dependentRelationship;

    private Boolean specialNeeds;

}
