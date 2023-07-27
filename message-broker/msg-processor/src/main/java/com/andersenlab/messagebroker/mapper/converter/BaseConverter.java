package com.andersenlab.messagebroker.mapper.converter;

public interface BaseConverter<F, T> {

    void convert(F from, T to);
}

