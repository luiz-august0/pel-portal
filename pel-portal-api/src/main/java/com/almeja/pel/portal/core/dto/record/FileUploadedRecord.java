package com.almeja.pel.portal.core.dto.record;

import java.math.BigDecimal;

public record FileUploadedRecord(
        String filename,
        BigDecimal size,
        boolean s3File
) {}
