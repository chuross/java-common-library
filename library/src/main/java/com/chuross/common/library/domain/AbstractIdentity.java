package com.chuross.common.library.domain;

public abstract class AbstractIdentity<T> extends AbstractValueObject implements Identity<T> {

    private T value;

    public AbstractIdentity(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
