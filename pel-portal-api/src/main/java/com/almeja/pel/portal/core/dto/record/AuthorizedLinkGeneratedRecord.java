package com.almeja.pel.portal.core.dto.record;

import java.util.Date;

public record AuthorizedLinkGeneratedRecord(
        String token,
        Date generatedAt,
        Date expires
) {
}
