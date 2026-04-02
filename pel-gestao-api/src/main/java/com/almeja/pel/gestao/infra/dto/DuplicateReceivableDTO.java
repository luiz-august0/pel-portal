package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.infra.annotation.ObjectFieldsOnly;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class DuplicateReceivableDTO extends BaseDTO {

    private Integer id;

    private DuplicateDTO duplicate;

    private BigDecimal lateInterestPercentage;

    private BigDecimal finePercentage;

    private String bankSlipNumber;

    private Boolean reissue;

    private Boolean collection;

}
