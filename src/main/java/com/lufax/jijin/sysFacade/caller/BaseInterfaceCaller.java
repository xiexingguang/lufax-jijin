package com.lufax.jijin.sysFacade.caller;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jersey.client.JerseyService;
import com.lufax.jersey.client.response.helper.GSONHelper;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.Response;


public abstract class BaseInterfaceCaller {

    protected abstract JerseyService getJerseyService();

    public <T> T get(InterfaceCallObject interfaceCallObject, Class<T> clazz) {
        try {
            T gson = GSONHelper.convert(getClientResponse(interfaceCallObject), clazz);
            Logger.info(this, String.format("[GET] request, response from [URL: %s]: %s.", interfaceCallObject.getUrl(), gson));
            return gson;
        } catch (RuntimeException e) {
            Logger.error(this, String.format("[GET] request to [URL: %s] failed.", interfaceCallObject.getUrl()), e);
            throw e;
        }
    }

    public <T> T getWithQueryParams(InterfaceCallObject interfaceCallObject, Class<T> clazz) {
        try {
            T gson = GSONHelper.convert(getClientResponseWithQueryParams(interfaceCallObject), clazz);
            Logger.info(this, String.format("[GET with QueryParams] request, response from [URL: %s]: %s.", interfaceCallObject.getUrl(), gson));
            return gson;
        } catch (RuntimeException e) {
            Logger.error(this, String.format("[GET with QueryParams] request to [URL: %s] failed.", interfaceCallObject.getUrl()), e);
            throw e;
        }
    }

    public <T> T post(InterfaceCallObject interfaceCallObject, Class<T> clazz) {
        try {
            T gson = GSONHelper.convert(postClientResponse(interfaceCallObject), clazz);
            Logger.info(this, String.format("[POST] request, response from [URL: %s]: %s.", interfaceCallObject.getUrl(), gson));
            return gson;
        } catch (RuntimeException e) {
            Logger.error(this, String.format("[POST] request to [URL: %s] failed.", interfaceCallObject.getUrl()), e);
            throw e;
        }
    }

    public String get(InterfaceCallObject interfaceCallObject) {
        try {
            String entity = getClientResponse(interfaceCallObject).getEntity(String.class);
            Logger.info(this, String.format("[POST] request, response from [URL: %s]: %s.", interfaceCallObject.getUrl(), entity));
            return entity;
        } catch (RuntimeException e) {
            Logger.error(this, String.format("[POST] request to [URL: %s] failed.", interfaceCallObject.getUrl()), e);
            throw e;
        }
    }

    public Response getResponse(InterfaceCallObject interfaceCallObject) {
        try {
            ClientResponse clientResponse = getClientResponse(interfaceCallObject);
            return Response.status(clientResponse.getStatus()).entity(clientResponse.getEntity(String.class)).build();
        } catch (Exception e) {
            Logger.error(this, String.format("Received no response from [URL: %s].", interfaceCallObject.getUrl()), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
        }
    }

    public Response getWithQueryParamsResponse(InterfaceCallObject interfaceCallObject) {
        try {
            ClientResponse clientResponse = getClientResponseWithQueryParams(interfaceCallObject);
            return Response.status(clientResponse.getStatus()).entity(clientResponse.getEntity(String.class)).build();
        } catch (Exception e) {
            Logger.error(this, String.format("Received no response from [URL: %s].", interfaceCallObject.getUrl()), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
        }
    }

    public Response postResponse(InterfaceCallObject interfaceCallObject) {
        try {
            ClientResponse clientResponse = postClientResponse(interfaceCallObject);
            return Response.status(clientResponse.getStatus()).entity(clientResponse.getEntity(String.class)).build();
        } catch (Exception e) {
            Logger.error(this, String.format("Received no response from [URL: %s].", interfaceCallObject.getUrl()), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
        }
    }

    public ClientResponse getClientResponse(InterfaceCallObject interfaceCallObject) {
        WebResource resource = getJerseyService()
                .getInstance(interfaceCallObject.getUrl())
                .withUser(interfaceCallObject.getUserId())
                .getResource();
        return resource.get(ClientResponse.class);
    }

    public ClientResponse getClientResponseWithQueryParams(InterfaceCallObject interfaceCallObject) {
        WebResource resource = getJerseyService()
                .getInstance(interfaceCallObject.getUrl())
                .withUser(interfaceCallObject.getUserId())
                .getResource();
        return resource.queryParams(interfaceCallObject.getQueryParams()).get(ClientResponse.class);
    }

    public ClientResponse postClientResponse(InterfaceCallObject interfaceCallObject) {
        WebResource resource = getJerseyService()
                .getInstance(interfaceCallObject.getUrl())
                .withUser(interfaceCallObject.getUserId())
                .getResource();
        return resource.post(ClientResponse.class, interfaceCallObject.getForm());
    }
}
