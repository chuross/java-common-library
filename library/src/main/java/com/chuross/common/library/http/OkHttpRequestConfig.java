package com.chuross.common.library.http;

import java.util.concurrent.TimeUnit;

public class OkHttpRequestConfig {

    private static final long DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(10);
    private long connectionTimeout;
    private long readTimeout;
    private long writeTimeout;
    private boolean followRedirects;

    private OkHttpRequestConfig(final long connectionTimeout, final long readTimeout, final long writeTimeout, final boolean followRedirects) {
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.followRedirects = followRedirects;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public static class Builder {

        private Long connectionTimeout;
        private Long readTimeout;
        private Long writeTimeout;
        private Boolean followRedirects;

        public Builder setConnectionTimeout(final long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setReadTimeout(final long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setWriteTimeout(final long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder setFollowRedirects(final boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        public OkHttpRequestConfig build() {
            final long connectionTimeout = this.connectionTimeout != null ? this.connectionTimeout : DEFAULT_TIMEOUT;
            final long readTimeout = this.readTimeout != null ? this.readTimeout : DEFAULT_TIMEOUT;
            final long writeTimeout = this.writeTimeout != null ? this.writeTimeout : DEFAULT_TIMEOUT;
            final boolean followRedirects = this.followRedirects != null ? this.followRedirects : true;
            return new OkHttpRequestConfig(connectionTimeout, readTimeout, writeTimeout, followRedirects);
        }
    }
}
