package com.almeja.pel.gestao.core.dto;

import com.almeja.pel.gestao.core.dto.base.BaseDTO;
import com.almeja.pel.gestao.core.dto.enums.EnumPortalDocumentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class PortalDocumentDTO extends BaseDTO {

    private UUID id;

    private EnumPortalDocumentType documentType;

    private String originalFilename;

    private String filename;

    private BigDecimal size;

    private Boolean s3File;

}
