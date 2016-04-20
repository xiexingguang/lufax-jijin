package com.lufax.jijin.base.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
	
    public static Map buildKeyValueMap(Object... conditions) {
        Map conditionMap = new HashMap();
        for (int i = 0; i < conditions.length; i += 2) {
            conditionMap.put(conditions[i], conditions[i + 1]);
        }
        return conditionMap;
    }

	
    public static Map<String, String> convertFromString(String mapAsString) {
        Map<String, String> map = new HashMap<String, String>();

        String actualInput = mapAsString.substring(1, mapAsString.length() - 1);

        String[] keyValuePairs = actualInput.split(",");
        for (String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.split("=");
            map.put(keyValue[0].trim(), keyValue.length > 1 ? keyValue[1].trim() : "");
        }

        return map;
    }
}
