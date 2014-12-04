package com.chuross.common.library.util;

import java.util.List;
import java.util.Map;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    private static <T> void addIfNotNull(final List<T> list, final T item) {
        if(item == null) {
            return;
        }
        list.add(item);
    }

    private static <K, T> void putIfNotNull(final Map<K, T> map, final K key, final T item) {
        if(key == null || item == null) {
            return;
        }
        map.put(key, item);
    }

}
