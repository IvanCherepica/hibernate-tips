package com.javahelps.jpa.test.test.util;

public class GraphPreparedWrapper<T> extends SimplePreparedWrapper<T> {

    public GraphPreparedWrapper(SimplePreparedWrapper<T> simplePreparedWrapper) {
        super(simplePreparedWrapper);
    }

    public GraphPreparedWrapper<T> with(String propertyName) {
        return null;
    }
}
