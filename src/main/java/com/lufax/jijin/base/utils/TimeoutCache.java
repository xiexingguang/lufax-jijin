package com.lufax.jijin.base.utils;

public abstract class TimeoutCache<T> {

    public abstract T refresh();

    public abstract long getTimeoutSecond();

}
