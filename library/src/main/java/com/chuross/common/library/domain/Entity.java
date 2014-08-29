package com.chuross.common.library.domain;

import java.io.Serializable;

public interface Entity<I extends Identity<?>, T extends Entity<I, T>> extends Cloneable, Serializable {

    public I getIdentity();

    public boolean equals(Object that);

    public int hashCode();

    public T clone();

}
