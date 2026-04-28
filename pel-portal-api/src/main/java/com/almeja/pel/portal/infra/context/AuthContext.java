package com.almeja.pel.portal.infra.context;

import com.almeja.pel.portal.core.domain.entity.UserEntity;
import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Getter
@Setter
public class AuthContext {

    private UserEntity user;

}
