package com.chuross.common.library.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> void addIfNotNull(final List<T> list, final T item) {
        if(item == null) {
            return;
        }
        list.add(item);
    }

    public static <K, T> void putIfNotNull(final Map<K, T> map, final K key, final T item) {
        if(key == null || item == null) {
            return;
        }
        map.put(key, item);
    }

    public static <K, T> void putIfNotNull(final ListMultimap<K, T> map, final K key, final T item) {
        if(key == null || item == null) {
            return;
        }
        map.put(key, item);
    }

    public static <K, V> void foreach(final Map<K, V> map, final Procedure<K, V> procedure) {
        for(final Map.Entry<K, V> entry : map.entrySet()) {
            procedure.process(entry.getKey(), entry.getValue());
        }
    }

    public static <K, V> void foreach(final ListMultimap<K, V> multimap, final Procedure<K, V> procedure) {
        for(final Map.Entry<K, Collection<V>> entry : multimap.asMap().entrySet()) {
            final K key = entry.getKey();
            for(final V value : entry.getValue()) {
                procedure.process(key, value);
            }
        }
    }

    public static <K, V> ListMultimap<K, V> toArrayListMultiMap(final Map<K, List<V>> map) {
        final ListMultimap<K, V> multimap = ArrayListMultimap.create();
        for(final Map.Entry<K, List<V>> entry : map.entrySet()) {
            multimap.putAll(entry.getKey(), entry.getValue());
        }
        return multimap;
    }
}
