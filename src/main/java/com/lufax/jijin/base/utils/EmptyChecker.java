package com.lufax.jijin.base.utils;

import java.util.Collection;
import java.util.Map;

public class EmptyChecker {

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        return false;
    }

    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(isEmpty(args));
    }
}
