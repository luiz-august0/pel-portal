package com.almeja.pel.portal.core.dto.record;

import com.almeja.pel.portal.core.domain.entity.UserEntity;

public record AuthorizedUserRecord(
        UserEntity user,
        String accessToken
) {
}

