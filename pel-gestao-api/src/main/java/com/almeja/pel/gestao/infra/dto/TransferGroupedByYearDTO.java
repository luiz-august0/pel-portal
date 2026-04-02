package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TransferGroupedByYearDTO extends BaseDTO {

    private String year;

    private List<TransferDTO> transfers;

}
