package com.almeja.pel.gestao.core.dto.record;

import java.math.BigDecimal;

public record FileUploadedRecord(
        String filename,
        BigDecimal size,
        boolean s3File
) {
}
