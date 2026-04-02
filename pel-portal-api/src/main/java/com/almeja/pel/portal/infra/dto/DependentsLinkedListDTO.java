package com.almeja.pel.portal.infra.dto;

import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DependentsLinkedListDTO extends BaseDTO {

    private List<DependentDTO> pending;

    private List<DependentDTO> active;

}
