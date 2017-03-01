package com.zenika.zencontact.resource.auth;

import java.io.IOException;

import restx.RestxContext;
import restx.RestxFilter;
import restx.RestxHandler;
import restx.RestxHandlerMatch;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatch;
import restx.factory.Component;
import restx.http.HttpStatus;

import com.google.common.base.Optional;

@Component
public class AuthFilter implements RestxFilter {

	private AppEngineAuthHandler authHandler;

	public AuthFilter(AppEngineAuthHandler authHandler) {
		this.authHandler = authHandler;
	}

	@Override
	public Optional<RestxHandlerMatch> match(RestxRequest req) {
		return Optional.of(new RestxHandlerMatch(new StdRestxRequestMatch(
				"*users*", req.getRestxPath()), authHandler));
	}

	@Override
	public String toString() {
		return "AuthFilter";
	}

	@Component
	public static class AppEngineAuthHandler implements RestxHandler {
		@Override
        public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws IOException {
			if(AuthenticationService.getInstance().getUser() != null && AuthenticationService.getInstance().getUsername() != null) {
				resp.setHeader("Username", AuthenticationService.getInstance().getUsername());
				resp.setHeader("Logout", AuthenticationService.getInstance().getLogoutURL("/#/clear"));
			}
			if(!req.getRestxPath().endsWith("users") && AuthenticationService.getInstance().getUser() == null) {
				resp.setHeader("Location", AuthenticationService.getInstance().getLoginURL("/#/edit/" + req.getRestxPath().split("/")[3]));
				resp.setHeader("Logout", AuthenticationService.getInstance().getLogoutURL("/#/clear"));
				resp.setStatus(HttpStatus.UNAUTHORIZED);
                return;
			}
			if (req.getHttpMethod() == "DELETE" && !AuthenticationService.getInstance().isAdmin()) {
				resp.setStatus(HttpStatus.FORBIDDEN);
				return;
			}
			ctx.nextHandlerMatch().handle(req, resp, ctx);	
		}
	}

}
