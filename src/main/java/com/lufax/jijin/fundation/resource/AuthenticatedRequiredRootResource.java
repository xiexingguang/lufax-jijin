package com.lufax.jijin.fundation.resource;

import com.lufax.jersey.usercontext.UserContextUtils;
import com.lufax.jersey.utils.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public abstract class AuthenticatedRequiredRootResource {
    @Context
    protected HttpServletRequest servletRequest;


    protected Long currentUser() {
        Long currentUser = null;
        if (UserContextUtils.getCurrentUserContext() != null) {
            currentUser = Long.parseLong(UserContextUtils.getCurrentUserContext().getUserId());
        }
        if (currentUser == null) {
            Logger.warn(this, "some user want to access resource in no login status");
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return currentUser;
    }
}
