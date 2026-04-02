package com.almeja.pel.gestao.infra.dto.mapper;

import com.almeja.pel.gestao.infra.dto.TransferDTO;
import com.almeja.pel.gestao.infra.dto.TransferGroupedByYearDTO;
import com.almeja.pel.gestao.infra.util.ConverterEntityToDTOUtil;

import java.util.List;

public class TransferGroupedByYearMapper {

    public static List<TransferGroupedByYearDTO> toDTO(List<com.almeja.pel.gestao.core.dto.TransferGroupedByYearDTO> transferGroupedByYear) {
        return transferGroupedByYear.stream().map(grouped -> new TransferGroupedByYearDTO(
                grouped.getYear(),
                ConverterEntityToDTOUtil.convert(grouped.getTransfers(), TransferDTO.class))).toList();
    }

}
