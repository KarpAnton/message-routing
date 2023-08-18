package org.akhome.mapper;

public interface Mapper {

    <F,T> T map(F from, T to);

    <F,T> T map(F from, Class<T> toClass);
}
