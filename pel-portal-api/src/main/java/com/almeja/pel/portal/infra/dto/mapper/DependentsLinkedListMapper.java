package com.almeja.pel.portal.infra.dto.mapper;

import com.almeja.pel.portal.core.util.ConverterEntityToDTOUtil;
import com.almeja.pel.portal.infra.dto.DependentDTO;
import com.almeja.pel.portal.infra.dto.DependentsLinkedListDTO;

public class DependentsLinkedListMapper {

    public static DependentsLinkedListDTO map(com.almeja.pel.portal.core.dto.DependentsLinkedListDTO dependentsLinkedListDTO) {
        return new DependentsLinkedListDTO(
                ConverterEntityToDTOUtil.convert(dependentsLinkedListDTO.getPending(), DependentDTO.class),
                ConverterEntityToDTOUtil.convert(dependentsLinkedListDTO.getActive(), DependentDTO.class)
        );
    }

}
