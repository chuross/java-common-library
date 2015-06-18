package com.chuross.common.library.util;

public interface Procedure<K, V> {

    void process(K key, V value);
}
