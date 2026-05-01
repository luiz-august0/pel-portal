package com.almeja.pel.portal.infra.service.crypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.almeja.pel.portal.core.gateway.crypt.UserCryptPasswordGTW;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserCryptPasswordService implements UserCryptPasswordGTW {

    @Override
    public String cryptPassword(String password) {
        return BCrypt.withDefaults().hashToString(10, password.toCharArray());
    }

}
