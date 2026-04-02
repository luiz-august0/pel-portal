package com.almeja.pel.gestao.infra.dto.mapper;

import com.almeja.pel.gestao.infra.dto.LevelingGroupedByYearDTO;
import com.almeja.pel.gestao.infra.dto.LevelingRegistrationDTO;
import com.almeja.pel.gestao.infra.util.ConverterEntityToDTOUtil;

import java.util.List;

public class LevelingGroupedByYearMapper {

    public static List<LevelingGroupedByYearDTO> toDTO(List<com.almeja.pel.gestao.core.dto.LevelingGroupedByYearDTO> levelingGroupedByYear) {
        return levelingGroupedByYear.stream().map(grouped -> new LevelingGroupedByYearDTO(
                grouped.getYear(),
                ConverterEntityToDTOUtil.convert(grouped.getLevelingRegistrations(), LevelingRegistrationDTO.class))).toList();
    }

}
