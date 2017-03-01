package com.zenika.zencontact.resource.email;

import com.zenika.zencontact.domain.Email;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

/**
 * Created by PierreG on 01/03/17.
 *
 */
@Component
@RestxResource
public class EmailResource {

    @POST("/v2/email")
    @PermitAll
    public void sendEmail(Email email) {
        EmailService.getInstance().sendEmail(email);
    }
}
