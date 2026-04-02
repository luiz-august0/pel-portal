package com.almeja.pel.portal.core.gateway.token;

public interface RecoveryTokenGTW {

    String validateRecoveryToken(String token);

    String generateRecoveryToken(String cpf);

}
