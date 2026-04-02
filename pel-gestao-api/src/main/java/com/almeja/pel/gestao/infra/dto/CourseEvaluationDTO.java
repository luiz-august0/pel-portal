package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class CourseEvaluationDTO extends BaseDTO {

    private Integer id;

    private String courseEvaluationName;

    private Integer groupNumber;

    private BigDecimal weight;

    private Boolean isOral;

}
