package com.chuross.common.library.domain;

import java.io.Serializable;

public interface ValueObject extends Serializable {

    public boolean equals(Object that);

    public int hashCode();
}
