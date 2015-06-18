package com.chuross.common.library.domain;

import java.io.Serializable;

public interface Entity<I extends Identity<?>, T extends Entity<I, T>> extends Cloneable, Serializable {

    I getIdentity();

    boolean sameIdentityAs(I identity);

    boolean equals(Object that);

    int hashCode();

    T clone();
}
