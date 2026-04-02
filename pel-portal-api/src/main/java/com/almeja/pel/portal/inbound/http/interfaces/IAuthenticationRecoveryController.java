package com.almeja.pel.portal.inbound.http.interfaces;

import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryPasswordRecord;
import com.almeja.pel.portal.core.dto.record.AuthenticationRecoveryRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(IAuthenticationRecoveryController.PATH)
public interface IAuthenticationRecoveryController {

    String PATH = IAuthenticationController.PATH + "/recovery";

    @PostMapping
    void generateRecovery(@RequestBody AuthenticationRecoveryRecord authenticationRecoveryRecord);

    @PostMapping("/password")
    void changePassword(@RequestBody AuthenticationRecoveryPasswordRecord authenticationRecoveryPasswordRecord);

}