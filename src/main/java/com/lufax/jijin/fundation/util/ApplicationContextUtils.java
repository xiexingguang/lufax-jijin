package com.lufax.jijin.fundation.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getSpringContext() {
        return context;
    }

    public static ApplicationContext getSpringContextForEmailTest() {
        return context;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static Object getBean(Class requiredType) {
        return context.getBean(requiredType);
    }
    
    public static <T> T getBean(String name, Class<T> requiredType) {
    	return context.getBean(name, requiredType);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}