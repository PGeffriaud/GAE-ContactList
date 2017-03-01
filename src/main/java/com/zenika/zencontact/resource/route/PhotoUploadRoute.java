package com.zenika.zencontact.resource.route;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.zenika.zencontact.domain.blob.PhotoService;

import restx.RestxContext;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatcher;
import restx.StdRoute;
import restx.factory.Component;
import restx.factory.Factory;

@Component
public class PhotoUploadRoute extends StdRoute {
    private final Factory factory;
    @Inject
    public PhotoUploadRoute(Factory factory) {
        super("FactoryRoute", new StdRestxRequestMatcher("POST", "/v2/users/{id}/photo"));
        this.factory = factory;
    }
    @Override
    public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws IOException {
    	HttpServletRequest httpServletRequest = req.unwrap(HttpServletRequest.class);
    	PhotoService.getInstance().updatePhoto(Long.valueOf(match.getPathParam("id")), httpServletRequest);
    	resp.setContentType("text/plain");
        resp.getWriter().println("ok");
    }
}