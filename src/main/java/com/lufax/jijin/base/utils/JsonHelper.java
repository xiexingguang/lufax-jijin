package com.lufax.jijin.base.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lufax.jijin.base.utils.DateUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

public class JsonHelper {

    public static JsonObject parse(String message) {
        return (JsonObject) new JsonParser().parse(message);
    }

    public static BigDecimal getAsBigDecimal(JsonObject jo, String memberName) {
        JsonElement je = jo.get(memberName);
        if (je == null) return null;
        return je.getAsBigDecimal();
    }

    public static String getAsString(JsonObject jo, String memberName) {
        JsonElement je = jo.get(memberName);
        if (je == null) return null;
        return je.getAsString();
    }

    public static int getAsInt(JsonObject jo, String memberName) {
        JsonElement je = jo.get(memberName);
        if (je == null) return 0;
        return je.getAsInt();
    }

    public static String buildJsonString(Object... objects) {
        JsonObject jsonObject = new JsonObject();
        if (objects.length % 2 != 0) {
            throw new RuntimeException("the parameter should be paired.");
        }
        for (int i = 0; i < objects.length; i += 2) {
            String key = objects[i].toString();
            Object value = objects[i + 1];
            if (value == null) {
                jsonObject.addProperty(key, "");
            } else if (value instanceof Number) {
                jsonObject.addProperty(key, (Number) value);
            } else if (value instanceof Boolean) {
                jsonObject.addProperty(key, (Boolean) value);
            } else if (value instanceof Date) {
                jsonObject.addProperty(key, DateUtils.formatDateTime((Date) value));
            } else {
                jsonObject.addProperty(key, value.toString());
            }
        }
        return jsonObject.toString();
    }
    
    public static String toJson(Object object) {
        return gsonObj().toJson(object);
    }
    
    public static String toJson(Object object, Type type) {
        return gsonObj().toJson(object, type);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gsonObj().fromJson(json, clazz);
    }
    
    private static final ThreadLocal<Gson> THREAD_LOCAL_GSON = new ThreadLocal<Gson>() {
		@Override
		protected Gson initialValue() {
			return new Gson();
		}
	};
	
	public static final Gson gsonObj() {
		if(null == THREAD_LOCAL_GSON.get()) {
			THREAD_LOCAL_GSON.set(new Gson());
		}
		return THREAD_LOCAL_GSON.get();
	}
}


