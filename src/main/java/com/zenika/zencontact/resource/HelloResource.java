package com.zenika.zencontact.resource;

import com.google.common.base.Optional;
import com.zenika.zencontact.domain.Message;
import org.joda.time.DateTime;
import restx.annotations.GET;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

@Component
@RestxResource
public class HelloResource {

    @GET("/message")
    @PermitAll
    public Message sayHello(String who) {
        Message msg = new Message();
        msg.message = String.format(
                "hello1 %s, it's %s",
                who, DateTime.now().toString("HH:mm:ss"));
        return msg;
    }
}

