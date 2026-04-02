package com.almeja.pel.portal.core.gateway.repository;

import com.almeja.pel.portal.core.domain.entity.AddressEntity;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepositoryGTW {

    AddressEntity save(AddressEntity address);

}
