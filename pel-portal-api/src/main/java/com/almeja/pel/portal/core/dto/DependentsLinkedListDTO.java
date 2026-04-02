package com.almeja.pel.portal.core.dto;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class DependentsLinkedListDTO extends BaseDTO {

    private final List<UserDependentEntity> pending;

    private final List<UserDependentEntity> active;

}
