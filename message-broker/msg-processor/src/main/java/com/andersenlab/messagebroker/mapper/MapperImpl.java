package com.andersenlab.messagebroker.mapper;

import com.andersenlab.messagebroker.mapper.annotation.Converter;
import com.andersenlab.messagebroker.mapper.converter.BaseConverter;
import com.andersenlab.messagebroker.mapper.exception.ConverterNotFoundException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MapperImpl implements Mapper, InitializingBean {

    private static Map<String, BaseConverter> mappedConverters;

    @Autowired
    private List<BaseConverter> registeredConverters;

    @Override
    public void afterPropertiesSet() throws Exception {
        // converter will be registered if it has @Converter annotation
        registeredConverters = registeredConverters.stream()
                .filter(converter -> converter.getClass().getAnnotation(Converter.class) != null)
                .collect(Collectors.toList());

        mappedConverters = new HashMap<>(registeredConverters.size());
        String key;
        for (BaseConverter converter : registeredConverters) {
            Class<? extends BaseConverter> converterClass = converter.getClass();
            Method method = getConverterMethod(converterClass.getMethods());
            if (method != null) {
                key = generateKey(method);
                mappedConverters.put(key, converter);
            }
        }
    }

    @Override
    public <F, T> T map(F from, T to) {
        BaseConverter<F, T> converter = findConverter(from.getClass(), to.getClass());
        converter.convert(from, to);
        return to;
    }

    @Override
    public <F, T> T map(F from, Class<T> toClass) {
        try {
            T toInstance = toClass.newInstance();
            return map(from, toInstance);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Method getConverterMethod(Method[] methods) {
        for (Method method : methods) {
            if (method.getName().equals("convert")) {
                return method;
            }
        }
        return null;
    }

    private String generateKey(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder key = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            key.append(clazz.getTypeName());
        }
        return key.toString();
    }

    private BaseConverter findConverter(Class<?> from, Class<?> to) {
        String key = from.getTypeName() + to.getTypeName();
        BaseConverter foundConverter = mappedConverters.get(key);
        if (foundConverter != null) {
            return foundConverter;
        } else {
            throw new ConverterNotFoundException(String.format("Cannot find converter from %s to %s", from.getTypeName(), to.getTypeName()));
        }
    }
}
