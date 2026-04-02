package com.almeja.pel.gestao.infra.dto;

import com.almeja.pel.gestao.core.domain.enums.EnumDuplicateStatus;
import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.infra.annotation.ObjectFieldsOnly;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class DuplicateDTO extends BaseDTO {

    private Integer id;

    private Date issueDate;

    private Date dueDate;

    private Date operationDate;

    private EnumDuplicateStatus status;

    private Integer installmentNumber;

    private Integer totalInstallments;

    private BigDecimal duplicateAmount;

    private BigDecimal discountAmount;

    private BigDecimal interestAmount;

    private BigDecimal operationAmount;

    private String observation;

}
