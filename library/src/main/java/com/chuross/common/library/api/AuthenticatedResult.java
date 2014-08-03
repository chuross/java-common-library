package com.chuross.common.library.api;

public interface AuthenticatedResult<T> extends Result<T> {

    public boolean isExpiredLoginSession();

}
