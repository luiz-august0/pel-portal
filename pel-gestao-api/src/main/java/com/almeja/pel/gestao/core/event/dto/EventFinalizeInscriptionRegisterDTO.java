package com.almeja.pel.gestao.core.event.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(of = "inscriptionId", callSuper = false)
public class EventFinalizeInscriptionRegisterDTO extends BaseDTO {

    private Integer inscriptionId;

    private String paymentForm;

    private Boolean internalCommunity;

}
