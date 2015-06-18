package com.chuross.common.library.domain;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class AbstractEntity<I extends Identity<?>, T extends Entity<I, T>> implements Entity<I, T> {

    private I identity;

    public AbstractEntity(final I identity) {
        this.identity = identity;
    }

    @Override
    public I getIdentity() {
        return identity;
    }

    @Override
    public boolean sameIdentityAs(final I identity) {
        return this.identity.equals(identity);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T clone() {
        return SerializationUtils.clone((T) this);
    }
}
