package com.almeja.pel.portal.infra.dto;

import com.almeja.pel.portal.core.domain.enums.EnumDependentRelationship;
import com.almeja.pel.portal.core.dto.UserDTO;
import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class DependentDTO extends BaseDTO {

    private UUID id;

    private UserDTO dependent;

    private EnumDependentRelationship dependentRelationship;

    private Boolean fromLink;

}
