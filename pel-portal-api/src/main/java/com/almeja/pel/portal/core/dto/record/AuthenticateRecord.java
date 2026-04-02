package com.almeja.pel.portal.core.dto.record;

public record AuthenticateRecord(
        String cpf,
        String password,
        String authorizedToken
) {
}