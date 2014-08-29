package com.chuross.common.library.domain;

import java.io.Serializable;

public interface Identity<T> extends Serializable {

    public T getIdentity();

}
