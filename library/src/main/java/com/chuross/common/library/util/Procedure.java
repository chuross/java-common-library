package com.chuross.common.library.util;

public interface Procedure<K, V> {

    public void process(K key, V value);
}
