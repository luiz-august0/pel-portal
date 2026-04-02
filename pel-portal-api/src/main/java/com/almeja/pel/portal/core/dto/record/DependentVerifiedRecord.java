package com.almeja.pel.portal.core.dto.record;

import com.almeja.pel.portal.core.domain.entity.UserDependentEntity;
import com.almeja.pel.portal.core.domain.entity.UserEntity;

public record DependentVerifiedRecord(
        UserEntity userDependent,
        UserDependentEntity dependentLink
) {
}
