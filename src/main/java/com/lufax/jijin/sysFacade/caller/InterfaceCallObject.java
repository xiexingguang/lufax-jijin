package com.lufax.jijin.sysFacade.caller;


import com.sun.jersey.api.representation.Form;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.math.BigDecimal;

public class InterfaceCallObject {

    private String userId = "";
    private String url = "";
    private Form form = new Form();
    private MultivaluedMapImpl queryParams = new MultivaluedMapImpl();

    public InterfaceCallObject withUserId(long userId) {
        this.userId = String.valueOf(userId);
        return this;
    }

    public InterfaceCallObject withUrl(String url) {
        this.url = url;
        return this;
    }

    public InterfaceCallObject withForm(Form form) {
        this.form = form;
        return this;
    }

    public InterfaceCallObject addFormValue(String key, Long value) {
        this.form.add(key, value);
        return this;
    }

    public InterfaceCallObject addFormValue(String key, String value) {
        this.form.add(key, value);
        return this;
    }

    public InterfaceCallObject addFormValue(String key, boolean value) {
        this.form.add(key, value);
        return this;
    }

    public InterfaceCallObject addFormValue(String key, BigDecimal value) {
        this.form.add(key, value);
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public String getUrl() {
        return url;
    }

    public Form getForm() {
        return form;
    }

    public MultivaluedMapImpl getQueryParams() {
        return queryParams;
    }

    public InterfaceCallObject addQueryParam(String key, String value) {
        this.queryParams.add(key, value);
        return this;
    }
}
