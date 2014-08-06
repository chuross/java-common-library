package com.chuross.common.library.api.result;

import com.chuross.common.library.api.result.Result;

public interface AuthenticatedResult<T> extends Result<T> {

    public boolean isExpiredLoginSession();

}
