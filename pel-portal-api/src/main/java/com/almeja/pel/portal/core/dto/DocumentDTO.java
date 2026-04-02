package com.almeja.pel.portal.core.dto;

import com.almeja.pel.portal.core.domain.enums.EnumDocumentType;
import com.almeja.pel.portal.core.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class DocumentDTO extends BaseDTO {

    private UUID id;

    private EnumDocumentType documentType;

    private String originalFilename;

    private String filename;

    private BigDecimal size;

    private Boolean s3File;

}
