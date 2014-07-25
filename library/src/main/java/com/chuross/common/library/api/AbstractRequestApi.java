package com.chuross.common.library.api;

import com.google.common.collect.Lists;
import org.apache.http.NameValuePair;

import java.util.List;

public abstract class AbstractRequestApi<T extends Result<?>> extends AbstractApi<T> {

    protected abstract void setParameters(List<NameValuePair> parameters);

    protected List<NameValuePair> getParameters() {
        List<NameValuePair> parameters = Lists.newArrayList();
        setParameters(parameters);
        return parameters;
    }

}
