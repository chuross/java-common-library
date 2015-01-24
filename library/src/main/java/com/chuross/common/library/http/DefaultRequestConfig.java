package com.chuross.common.library.http;

public class DefaultRequestConfig {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    private static final int DEFAULT_READ_TIMEOUT = 5000;
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;
    private boolean allowRedirect = true;

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isAllowRedirect() {
        return allowRedirect;
    }

    public void setAllowRedirect(final boolean allowRedirect) {
        this.allowRedirect = allowRedirect;
    }
}
