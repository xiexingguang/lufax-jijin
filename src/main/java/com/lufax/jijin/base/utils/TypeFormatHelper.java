package com.lufax.jijin.base.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class TypeFormatHelper {

    public static String formateTypes(String itemName, Object object) {
        try {
            if (object == null) {
                return (itemName + " : null\n");
            } else if (object instanceof List) {
                return listToString(itemName, (List) object);
            } else if (object instanceof HashMap) {
                return mapToString(itemName, (Map) object);
            } else if (isComplexType(object)) {
                return selfDefinedTypeToString(itemName, object);
            } else {
                return (itemName + " : " + object.toString() + "\n");
            }
        } catch (Exception e) {
            return "<Logging failed> Could not print the object: " + itemName;
        }
    }

    private static String listToString(String itemName, List list) {
        StringBuffer buffer = new StringBuffer("\n\t< " + itemName + " >\n");
        Iterator iterator = list.iterator();

        for (; iterator.hasNext(); ) {
            buffer.append("\t\t< ").append(iterator.next().toString()).append(" >\n");
        }

        buffer.append("\t< " + itemName + " >");
        return buffer.toString();
    }

    private static String mapToString(String itemName, Map map) {
        StringBuffer buffer = new StringBuffer("\n\t< " + itemName + " >\n");
        Iterator iterator = map.keySet().iterator();

        for (; iterator.hasNext(); ) {
            Object key = iterator.next();
            Object value = map.get(key);
            buffer.append("\t\t< ").append(null == key ? "null" : key.toString()).append(" = ").append(null == value ? "null" : value.toString()).append(" >\n");
        }

        buffer.append("\t< " + itemName + " >");
        return buffer.toString();
    }

    private static String selfDefinedTypeToString(String itemName, Object object) {
        StringBuffer buffer = new StringBuffer("\n\t< " + itemName + " >\n");

        try {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            while (clazz != null && !clazz.getName().equals("java.lang.Object")) {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    if (!Modifier.isStatic(fields[i].getModifiers())) {
                        buffer.append("\t\t< ").append(fields[i].getName()).append(" = ").append(fields[i].get(object)).append(" >\n");
                    }
                }
                clazz = clazz.getSuperclass();
            }
            buffer.append("\t< " + itemName + " >");
        } catch (IllegalAccessException iae) {
            buffer.append(iae.toString()).append("Cannot print this" + itemName + "type");
        }
        return buffer.toString();
    }

    //todo:[hanj] trying to provide a common meth for all type in the following part

    private static String complexTypeToString(String scope, Object parentObject, List visitedObjs) {
        StringBuffer buffer = new StringBuffer("");
        try {
            Class cl = parentObject.getClass();
            while (cl != null) {
                processFields(cl.getDeclaredFields(), scope, parentObject, buffer, visitedObjs);
                cl = cl.getSuperclass();
            }
        } catch (IllegalAccessException iae) {
            buffer.append(iae.toString());
        }
        return (buffer.toString());
    }

    private static void processFields(Field[] fields, String scope, Object parentObject, StringBuffer buffer, List visitedObjs) throws IllegalAccessException {
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (!Modifier.isStatic(fields[i].getModifiers())) {
                buffer.append(typeToString(scope + "." + fields[i].getName(), fields[i].get(parentObject), visitedObjs));
            }
        }
    }

    public static boolean isCollectionType(Object obj) {
        return (obj.getClass().isArray()
                || (obj instanceof Collection)
                || (obj instanceof Hashtable)
                || (obj instanceof HashMap)
                || (obj instanceof HashSet)
                || (obj instanceof List)
                || (obj instanceof AbstractMap));
    }

    public static boolean isComplexType(Object obj) {
        if (obj instanceof Boolean
                || obj instanceof Short
                || obj instanceof Byte
                || obj instanceof Integer
                || obj instanceof Long
                || obj instanceof Float
                || obj instanceof Character
                || obj instanceof Double
                || obj instanceof String) {
            return false;
        } else {
            Class objectClass = obj.getClass();
            if (objectClass == boolean.class
                    || objectClass == Boolean.class
                    || objectClass == short.class
                    || objectClass == Short.class
                    || objectClass == byte.class
                    || objectClass == Byte.class
                    || objectClass == int.class
                    || objectClass == Integer.class
                    || objectClass == long.class
                    || objectClass == Long.class
                    || objectClass == float.class
                    || objectClass == Float.class
                    || objectClass == char.class
                    || objectClass == Character.class
                    || objectClass == double.class
                    || objectClass == Double.class
                    || objectClass == String.class) {
                return false;
            } else {
                return true;
            }
        }
    }

    private static String collectionTypeToString(String scope, Object obj, List visitedObjs) {
        StringBuffer buffer = new StringBuffer("");

        if (obj.getClass().isArray()) {
            if (Array.getLength(obj) > 0) {
                for (int j = 0; j < Array.getLength(obj); j++) {
                    Object x = Array.get(obj, j);
                    buffer.append(typeToString(scope + "[" + j + "]", x, visitedObjs));
                }
            } else {
                buffer.append(scope + "[]: empty\n");
            }
        } else {
            boolean isCollection = (obj instanceof Collection);
            boolean isHashTable = (obj instanceof Hashtable);
            boolean isHashMap = (obj instanceof HashMap);
            boolean isHashSet = (obj instanceof HashSet);
            boolean isAbstractMap = (obj instanceof AbstractMap);
            boolean isMap = isAbstractMap || isHashMap || isHashTable;

            if (isMap) {
                Set keySet = ((Map) obj).keySet();
                Iterator iterator = keySet.iterator();
                int size = keySet.size();

                if (size > 0) {
                    for (int j = 0; iterator.hasNext(); j++) {
                        Object key = iterator.next();
                        Object x = ((Map) obj).get(key);
                        buffer.append(typeToString(scope + "[\"" + key + "\"]", x, visitedObjs));
                    }
                } else {
                    buffer.append(scope + "[]: empty\n");
                }
            } else if (/* isHashTable || */isCollection || isHashSet /* || isHashMap */) {
                Iterator iterator = null;
                int size = 0;

                if (obj != null) {
                    if (isCollection) {
                        iterator = ((Collection) obj).iterator();
                        size = ((Collection) obj).size();
                    } else if (isHashTable) {
                        iterator = ((Hashtable) obj).values().iterator();
                        size = ((Hashtable) obj).size();
                    } else if (isHashSet) {
                        iterator = ((HashSet) obj).iterator();
                        size = ((HashSet) obj).size();
                    } else if (isHashMap) {
                        iterator = ((HashMap) obj).values().iterator();
                        size = ((HashMap) obj).size();
                    }

                    if (size > 0) {
                        for (int j = 0; iterator.hasNext(); j++) {
                            Object x = iterator.next();
                            buffer.append(typeToString(scope + "[" + j + "]", x, visitedObjs));
                        }
                    } else {
                        buffer.append(scope + "[]: empty\n");
                    }
                } else { // theObject is null
                    buffer.append(scope + "[]: null\n");
                }
            }
        }
        return (buffer.toString());
    }

    private static String typeToString(String scope, Object obj, List visitedObjs) {
        if (obj == null) {
            return (scope + ": null\n");
        } else if (isCollectionType(obj)) {
            return collectionTypeToString(scope, obj, visitedObjs);
        } else if (isComplexType(obj)) {
            if (!visitedObjs.contains(obj)) {
                visitedObjs.add(obj);
                return complexTypeToString(scope, obj, visitedObjs);
            } else {
                return (scope + ": <already visited>\n");
            }
        } else {
            return (scope + ": " + obj.toString() + "\n");
        }
    }

    private static String typeToString(String scope, Object obj) {
        if (obj == null) {
            return (scope + ": null\n");
        } else if (isCollectionType(obj)) {
            return collectionTypeToString(scope, obj, new ArrayList());
        } else if (isComplexType(obj)) {
            return complexTypeToString(scope, obj, new ArrayList());
        } else {
            return (scope + ": " + obj.toString() + "\n");
        }
    }

    private static String checkValue(String s) {
        if (s == null)
            return "";
        if (s.startsWith("$")) {
            s = "";
        }
        return s;
    }

    private static boolean stringIsEmpty(String input) {
        if (input != null && !input.trim().equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

}
