package com.zenika.zencontact.resource.route;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import restx.RestxContext;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatcher;
import restx.StdRoute;
import restx.factory.Component;
import restx.factory.Factory;

import com.google.appengine.api.blobstore.BlobKey;
import com.zenika.zencontact.domain.blob.PhotoService;

@Component
public class PhotoDownloadRoute extends StdRoute {
    private final Factory factory;
    @Inject
    public PhotoDownloadRoute(Factory factory) {
        super("FactoryRoute", new StdRestxRequestMatcher("GET", "/v2/users/{id}/photo/{key}"));
        this.factory = factory;
    }
    @Override
    public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws IOException {
    	HttpServletResponse httpServletResponse = resp.unwrap(HttpServletResponse.class);
    	PhotoService.getInstance().serve(new BlobKey(match.getPathParam("key")), httpServletResponse);
    }
}