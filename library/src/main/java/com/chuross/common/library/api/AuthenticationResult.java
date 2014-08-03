package com.chuross.common.library.api;

public interface AuthenticationResult<S, T> extends Result<T> {

    public boolean isInvalidAccountInfo();

    public S getSession();

}
