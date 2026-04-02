package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GradeByQuarterDTO extends BaseDTO {

    private Integer quarter;

    private BigDecimal grade;

    private List<GradeDTO> grades;

}
