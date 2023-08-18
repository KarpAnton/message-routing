package org.akhome.mapper.converter;

public interface BaseConverter<F, T> {

    void convert(F from, T to);
}

