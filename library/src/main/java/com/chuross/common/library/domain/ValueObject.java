package com.chuross.common.library.domain;

import java.io.Serializable;

public interface ValueObject extends Serializable {

    boolean equals(Object that);

    int hashCode();
}
