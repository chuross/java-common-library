package com.chuross.common.library.api;

import com.chuross.common.library.api.result.AuthenticatedResult;
import com.chuross.common.library.api.result.AuthenticationResult;
import com.chuross.common.library.util.FutureUtils;
import com.chuross.common.library.util.MethodCallUtils;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.client.config.RequestConfig;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class AccountService<ACCOUNT, SESSION, AUTH_RESULT extends AuthenticationResult<SESSION, ?>> {

    private OnLoginSessionChangedListener<ACCOUNT, SESSION> listener;

    protected abstract Callable<Api<AUTH_RESULT>> getAuthenticationApiCallable(ACCOUNT account);

    public void setOnLoginSessionChangedListener(final OnLoginSessionChangedListener<ACCOUNT, SESSION> listener) {
        this.listener = listener;
    }

    protected <RESULT extends AuthenticatedResult<?>> Future<RESULT> executeWithAuthentication(final Executor executor, final ACCOUNT account, final RequestConfig config, final int retryCount, final Callable<Api<RESULT>> apiCallable) {
        return FutureUtils.executeOrNull(executor, new Callable<RESULT>() {
            @Override
            public RESULT call() throws Exception {
                return executeWithAuthentication(account, config, retryCount, apiCallable);
            }
        });
    }

    private <RESULT extends AuthenticatedResult<?>> RESULT executeWithAuthentication(final ACCOUNT account, final RequestConfig config, final int retryCount, final Callable<Api<RESULT>> apiCallable) {
        final Api<RESULT> api = MethodCallUtils.callOrNull(apiCallable);
        if(api == null) {
            return null;
        }
        final RESULT result = FutureUtils.getOrNull(api.execute(MoreExecutors.sameThreadExecutor(), config, retryCount));
        if(result == null) {
            return null;
        }
        if(!result.isExpiredLoginSession()) {
            return result;
        }
        final Api<AUTH_RESULT> authApi = MethodCallUtils.callOrNull(getAuthenticationApiCallable(account));
        if(authApi == null) {
            return null;
        }
        final AUTH_RESULT authResult = FutureUtils.getOrNull(authApi.execute(MoreExecutors.sameThreadExecutor(), config, retryCount));
        if(!authResult.isInvalidAccountInfo() && listener != null) {
            listener.onAuthenticationFailed(account);
        }
        if(!authResult.isSuccess()) {
            return result;
        }
        final SESSION newSession = authResult.getSession();
        if(listener != null) {
            listener.onSessionChanged(newSession);
        }
        return FutureUtils.getOrNull(MethodCallUtils.callOrNull(apiCallable).execute(MoreExecutors.sameThreadExecutor(), config, retryCount));
    }

    public interface OnLoginSessionChangedListener<ACCOUNT, SESSION> {

        public void onAuthenticationFailed(ACCOUNT account);

        public void onSessionChanged(SESSION session);
    }
}
