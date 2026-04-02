package com.almeja.pel.portal.core.gateway.authorization;

import com.almeja.pel.portal.core.dto.record.AuthenticateRecord;
import com.almeja.pel.portal.core.dto.record.AuthorizedUserRecord;

public interface AuthorizationGTW {

    AuthorizedUserRecord authorize(AuthenticateRecord authenticateRecord);

}
