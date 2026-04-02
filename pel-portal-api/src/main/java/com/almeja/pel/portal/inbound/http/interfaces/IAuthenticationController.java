package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.dto.UserRegisterDTO;
import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticatedRecord;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.almeja.pel.portal.infra.constants.PrefixPathConstant.PREFIX_PATH;

@RequestMapping(IAuthenticationController.PATH)
public interface IAuthenticationController {

    String PATH = PREFIX_PATH + "/auth";

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    void register(@RequestBody UserRegisterDTO userRegisterDTO);

    @PostMapping("/login")
    AuthenticatedRecord authenticate(@RequestBody AuthenticateRecord authenticationRecord);

}
