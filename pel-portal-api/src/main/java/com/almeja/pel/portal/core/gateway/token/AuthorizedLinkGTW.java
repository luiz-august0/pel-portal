package com.almeja.pel.portal.core.gateway.token;

import com.almeja.pel.portal.core.dto.record.AuthorizedLinkGeneratedRecord;
import com.almeja.pel.portal.core.dto.record.AuthorizedTokenRecord;

public interface AuthorizedLinkGTW {

    AuthorizedLinkGeneratedRecord generateResponsibleLink(String cpf);

    AuthorizedTokenRecord validateToken(String token);

}
