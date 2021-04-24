package com.javahelps.jpa.test.util;

public enum IsolationLevel {
    READ_UNCOMMITTED(1),
    READ_COMMITTED(2),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);

    private int value;

    IsolationLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
