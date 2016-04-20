package com.lufax.jijin.base.resource;

import com.lufax.jersey.usercontext.UserContextUtils;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.EmptyChecker;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public abstract class AuthenticatedRequiredRootResource {
    @Context
    protected HttpServletRequest servletRequest;


    protected Long currentUserAsLong() {
        return Long.parseLong(currentUserAsString());
    }

    protected String currentUserAsString() {

        if (UserContextUtils.getCurrentUserContext() != null && !EmptyChecker.isEmpty(UserContextUtils.getCurrentUserContext().getUserId())) {
            return UserContextUtils.getCurrentUserContext().getUserId();
        }

        Logger.warn(this, "some user want to access resource in no login status");
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}
