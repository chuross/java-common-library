package com.chuross.common.library.api;

import com.chuross.common.library.http.EnclosingRequestParameter;
import com.google.common.collect.Lists;
import org.apache.http.NameValuePair;

import java.util.List;

public abstract class AbstractEnclosingRequestApi<T extends Result<?>> extends AbstractApi<T> {

    protected abstract void setParameters(List<NameValuePair> parameters);

    protected EnclosingRequestParameter getParameter() {
        List<NameValuePair> parameters = Lists.newArrayList();
        setParameters(parameters);
        return new EnclosingRequestParameter(parameters);
    }

}