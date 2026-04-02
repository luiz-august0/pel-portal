package com.almeja.pel.gestao.core.event.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(of = "cpf", callSuper = false)
public class EventPersonDeclarationPdfGeneratedDTO extends BaseDTO {

    private String cpf;

    private byte[] declarationPdf;

}
