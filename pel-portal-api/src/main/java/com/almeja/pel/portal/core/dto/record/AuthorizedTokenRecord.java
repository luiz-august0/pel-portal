package com.almeja.pel.portal.core.dto.record;

import com.almeja.pel.portal.core.domain.enums.EnumAuthorizedLinkType;

public record AuthorizedTokenRecord(
        String cpf,
        boolean authorized,
        EnumAuthorizedLinkType authorizedLinkType
) {
}