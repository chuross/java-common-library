package com.chuross.common.library.api.result;

import com.chuross.common.library.api.result.Result;

public interface AuthenticationResult<S, T> extends Result<T> {

    public boolean isInvalidAccountInfo();

    public S getSession();

}
