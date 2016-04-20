package com.lufax.jijin.base.utils;

import java.util.HashMap;

public class TimeoutCacheManager {
    private static HashMap<String, TimeoutCache> cacheHolder = new HashMap<String, TimeoutCache>();
    private static HashMap<String, Long> timeoutSecondHolder = new HashMap<String, Long>();

    private static HashMap cacheDataHolder = new HashMap();
    private static HashMap<String, Long> lastCacheTimeHolder = new HashMap<String, Long>();

    public static synchronized void register(String id, TimeoutCache cache) {
        _addToHolder(id, cache);
    }

    public static <T> T getCacheData(String id) {
        Long lastCacheTime = lastCacheTimeHolder.get(id);
        Long timeoutSecond = timeoutSecondHolder.get(id);
        if (lastCacheTime == null || timeoutSecond == null) {
            throw new RuntimeException("The cache id does not exist. id=" + id);
        }
        if (System.currentTimeMillis() - lastCacheTime > timeoutSecond * 1000L) {
            TimeoutCache timeoutCache = cacheHolder.get(id);
            synchronized (timeoutCache) {
                if (System.currentTimeMillis() - lastCacheTimeHolder.get(id) > timeoutSecond * 1000L) {
                    T cacheData = (T) timeoutCache.refresh();
                    cacheDataHolder.put(id, cacheData);
                    lastCacheTimeHolder.put(id, System.currentTimeMillis());
                }
            }
        }
        return (T) cacheDataHolder.get(id);
    }

    public static synchronized void remove(String id) {
        cacheHolder.remove(id);
    }

    public static boolean contains(String id) {
        return cacheHolder.containsKey(id);
    }

    private static void _addToHolder(String id, TimeoutCache cache) {
        if (id == null || cache == null) return;
        cacheHolder.put(id, cache);
        timeoutSecondHolder.put(id, cache.getTimeoutSecond());
        lastCacheTimeHolder.put(id, 0L);
    }

    public static synchronized void refresh(String id) {
        _addToHolder(id, cacheHolder.get(id));
    }
}
