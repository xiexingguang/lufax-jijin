package com.lufax.jijin.base.utils;

import java.util.ArrayList;
import java.util.List;


public class ListUtils {


    public static <T> List<List<T>> split(final List<T> orginalList, final int splitSize) {
        int startIndex = 0;
        int endIndex = splitSize;
        List<List<T>> lists = new ArrayList<List<T>>();
        for (int i = 1; ; ) {
            if (endIndex <= orginalList.size()) {
                lists.add(orginalList.subList(startIndex, endIndex));
                startIndex = endIndex;
                ++i;
                endIndex = i * splitSize;
            } else if (startIndex < orginalList.size()) {
                lists.add(orginalList.subList(startIndex, orginalList.size()));
                break;
            } else
                break;

        }
        return lists;
    }
}
