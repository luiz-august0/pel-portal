package com.almeja.pel.gestao.infra.dto.mapper;

import com.almeja.pel.gestao.infra.dto.InscriptionDTO;
import com.almeja.pel.gestao.infra.dto.InscriptionGroupedByYearDTO;
import com.almeja.pel.gestao.infra.util.ConverterEntityToDTOUtil;

import java.util.List;

public class InscriptionGroupedByYearMapper {

    public static List<InscriptionGroupedByYearDTO> toDTO(List<com.almeja.pel.gestao.core.dto.InscriptionGroupedByYearDTO> inscriptionGroupedByYear) {
        return inscriptionGroupedByYear.stream().map(grouped -> new InscriptionGroupedByYearDTO(
                grouped.getYear(),
                ConverterEntityToDTOUtil.convert(grouped.getInscriptions(), InscriptionDTO.class))).toList();
    }

}
