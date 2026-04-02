package com.almeja.pel.gestao.core.event.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(of = "inscriptionId", callSuper = false)
public class EventInscriptionCertificatePdfGeneratedDTO extends BaseDTO {

    private Integer inscriptionId;

    private byte[] certificatePdf;

}
