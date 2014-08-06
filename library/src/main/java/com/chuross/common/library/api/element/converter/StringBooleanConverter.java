package com.chuross.common.library.api.element.converter;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class StringBooleanConverter implements Converter<Boolean> {

    @Override
    public Boolean read(InputNode node) throws Exception {
        return node.getValue().equals("true");
    }

    @Override
    public void write(OutputNode node, Boolean value) throws Exception {
        node.setValue(value ? "true" : "false");
    }

}
