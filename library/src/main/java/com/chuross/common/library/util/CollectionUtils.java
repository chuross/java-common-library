package com.chuross.common.library.util;

import java.util.List;
import java.util.Map;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    private static <T> void addIfNotNull(List<T> list, T item) {
        if(item == null) {
            return;
        }
        list.add(item);
    }

    private static <K, T> void putIfNotNull(Map<K, T> map, K key, T item) {
        if(key == null || item == null) {
            return;
        }
        map.put(key, item);
    }

}
