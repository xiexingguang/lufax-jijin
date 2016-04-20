package com.lufax.jijin.base.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

public class BeanUtils {
    private static ThreadLocal<Set> set = new ThreadLocal<Set>() {
        public Set initialValue() {
            return new HashSet();
        }
    };

    public static Map<String, Object> bean2map(Object bean) {
        return bean2map(bean, new HashSet());
    }

    private static Map<String, Object> bean2map(Object bean, Set hashSet) {
        if (EmptyChecker.isEmpty(bean) || hashSet.contains(bean)) {
            return null;
        }

        hashSet.add(bean);

        Map<String, Object> map = new HashMap<String, Object>();
        BeanWrapper bw = new BeanWrapperImpl(bean);

        for (PropertyDescriptor prop : bw.getPropertyDescriptors()) {
            String name = prop.getName();

            if (!bw.isReadableProperty(name)) {
                continue;
            }

            Object value = bw.getPropertyValue(name);
            if (value == null) {
                map.put(name, value);
                continue;
            }

            if (value.getClass().getName().startsWith("java") || Enum.class.isAssignableFrom(value.getClass())) {
                map.put(name, value);
                continue;
            }

            map.put(name, bean2map(value, hashSet));
        }

        hashSet.remove(bean);
        return map;
    }
    
	public static <T> List<T> getBeanPropertyList(final Collection beanList, String propertyname, boolean unique) {
		List<T> result = new ArrayList<T>();
		if(CollectionUtils.isNotEmpty(beanList)){
			for (Object bean : beanList) {
				try {
					T pv = (T) PropertyUtils.getProperty(bean, propertyname);
					if (pv != null && (!unique || !result.contains(pv)))
						result.add(pv);
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

}