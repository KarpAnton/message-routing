package com.andersenlab.messagebroker.service;

public abstract class BaseTest {

    public String name(String name) {
        return name + "-" + postfix();
    }

    private String postfix() {
        return getClass().getCanonicalName();
    }
}
