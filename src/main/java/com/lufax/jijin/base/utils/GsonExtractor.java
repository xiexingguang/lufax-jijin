package com.lufax.jijin.base.utils;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.util.List;

public class GsonExtractor {
    public static <T, V> String extractGson(List<T> collectionObject, List<V> gsons, Class gsonClazz) {
        for (T modelObject : collectionObject) {
            try {
                Constructor constructor = gsonClazz.getConstructor(modelObject.getClass());
                gsons.add((V) constructor.newInstance(modelObject));
            } catch (Exception e) {
                Logger.error(GsonExtractor.class, "Failed to parser object to json", e);
            }
        }
        return new Gson().toJson(gsons, gsons.getClass());
    }

    public static <T, V> List<V> extractGsonList(List<T> collectionObject, List<V> gsons, Class gsonClazz) {
        for (T modelObject : collectionObject) {
            try {
                Constructor constructor = gsonClazz.getConstructor(modelObject.getClass());
                gsons.add((V) constructor.newInstance(modelObject));
            } catch (Exception e) {
                Logger.error(GsonExtractor.class, "Failed to parser object to json", e);
            }
        }
        return gsons;
    }
}
