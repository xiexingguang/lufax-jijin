package com.lufax.jijin.product.dto;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class RemoteCallerException extends RuntimeException {

    public static ReloadableResourceBundleMessageSource ERROR_MESSAGE_RESOURCE = new ReloadableResourceBundleMessageSource();

    static {
        ERROR_MESSAGE_RESOURCE.setBasename("errors");
        ERROR_MESSAGE_RESOURCE.setDefaultEncoding("UTF-8");
    }

    public RemoteCallerException() {
    }

    public RemoteCallerException(String msg) {
        super(msg);
    }
}
