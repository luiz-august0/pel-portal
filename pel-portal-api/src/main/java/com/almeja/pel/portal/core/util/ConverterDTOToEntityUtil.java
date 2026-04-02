package com.almeja.pel.portal.core.util;

import com.almeja.pel.portal.core.domain.entity.base.BaseEntity;
import com.almeja.pel.portal.core.dto.base.BaseDTO;
import org.modelmapper.ModelMapper;

import java.util.List;

public class ConverterDTOToEntityUtil {

    private static final ModelMapper mapper = new ModelMapper();

    public static <Entity extends BaseEntity, DTO extends BaseDTO> Entity convert(DTO dto, Class<Entity> entityClass) {
        return mapper.map(dto, entityClass);
    }

    public static <Entity extends BaseEntity, DTO extends BaseDTO> List<Entity> convert(List<DTO> dtoList, Class<Entity> entityClass) {
        return dtoList.stream().map(dto -> convert(dto, entityClass)).toList();
    }

}