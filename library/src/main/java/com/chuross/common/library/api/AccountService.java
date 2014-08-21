package com.chuross.common.library.api;

import com.chuross.common.library.api.result.AuthenticatedResult;
import com.chuross.common.library.api.result.AuthenticationResult;
import com.chuross.common.library.util.FutureUtils;
import com.chuross.common.library.util.MethodCallUtils;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.client.config.RequestConfig;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class AccountService<SESSION, AUTH_RESULT extends AuthenticationResult<SESSION, ?>> {

    private OnLoginSessionChangedListener<SESSION> listener;

    protected abstract void onLoginSessionChanged(SESSION session);

    protected abstract Callable<Api<AUTH_RESULT>> getAuthenticationApiCallable();

    public void setOnLoginSessionChangedListener(OnLoginSessionChangedListener<SESSION> listener) {
        this.listener = listener;
    }

    protected <RESULT extends AuthenticatedResult<?>> Future<RESULT> executeWithAuthentication(Executor executor, final RequestConfig config, final int retryCount, final Callable<Api<RESULT>> apiCallable) {
        return FutureUtils.executeOrNull(executor, new Callable<RESULT>() {
            @Override
            public RESULT call() throws Exception {
                return executeWithAuthentication(config, retryCount, apiCallable);
            }
        });
    }

    private <RESULT extends AuthenticatedResult<?>> RESULT executeWithAuthentication(RequestConfig config, int retryCount, final Callable<Api<RESULT>> apiCallable) {
        Api<RESULT> api = MethodCallUtils.callOrNull(apiCallable);
        if(api == null) {
            return null;
        }
        RESULT result = FutureUtils.getOrNull(api.execute(MoreExecutors.sameThreadExecutor(), config, retryCount));
        if(result == null) {
            return null;
        }
        if(!result.isExpiredLoginSession()) {
            return result;
        }
        Api<AUTH_RESULT> authApi = MethodCallUtils.callOrNull(getAuthenticationApiCallable());
        if(authApi == null) {
            return null;
        }
        AUTH_RESULT authResult = FutureUtils.getOrNull(authApi.execute(MoreExecutors.sameThreadExecutor(), config, retryCount));
        if(!authResult.isInvalidAccountInfo() && listener != null) {
            listener.onAccountChanged();
        }
        if(!authResult.isSuccess()) {
            return result;
        }
        SESSION newSession = authResult.getSession();
        if(listener != null) {
            listener.onSessionChanged(newSession);
        }
        onLoginSessionChanged(newSession);
        return FutureUtils.getOrNull(MethodCallUtils.callOrNull(apiCallable).execute(MoreExecutors.sameThreadExecutor(), config, retryCount));
    }

    public interface OnLoginSessionChangedListener<SESSION> {
        public void onAccountChanged();
        public void onSessionChanged(SESSION session);
    }

}
