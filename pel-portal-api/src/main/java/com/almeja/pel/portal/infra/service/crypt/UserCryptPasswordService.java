package com.almeja.pel.portal.infra.service.crypt;

import com.almeja.pel.portal.core.gateway.crypt.UserCryptPasswordGTW;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCryptPasswordService implements UserCryptPasswordGTW {

    @Override
    public String cryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}
