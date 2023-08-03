package com.andersenlab.messagebroker.mapper;

import com.andersenlab.messagebroker.mapper.annotation.Converter;
import com.andersenlab.messagebroker.mapper.converter.BaseConverter;
import com.andersenlab.messagebroker.mapper.exception.ConverterNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MapperImpl implements Mapper, InitializingBean {

    private final static String KEY_DELIMITER = "&&";

    private Map<String, BaseConverter<?, ?>> mappedConverters;

    @Autowired
    private List<BaseConverter<?, ?>> registeredConverters;

    @Override
    public void afterPropertiesSet() {
        // converter will be registered if it has @Converter annotation
        registeredConverters = registeredConverters.stream()
                .filter(converter -> converter.getClass().getAnnotation(Converter.class) != null)
                .collect(Collectors.toList());

        mappedConverters = new HashMap<>(registeredConverters.size());
        String key;
        for (BaseConverter converter : registeredConverters) {
            Class<? extends BaseConverter> converterClass = converter.getClass();
            Method method = getConverterMethod(converterClass);
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

    private Method getConverterMethod(Class<? extends BaseConverter> converterClass) {
        Class<?>[] parameters = Arrays.stream(converterClass.getGenericInterfaces())
                .map(ParameterizedType.class::cast)
                .filter(cl -> cl.getRawType().equals(BaseConverter.class))
                .map(ParameterizedType::getActualTypeArguments)
                .flatMap(Arrays::stream)
                .map(Class.class::cast)
                .toArray(Class<?>[]::new);

        try {
            return converterClass.getMethod("convert", parameters);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String generateKey(Method method) {
        String fromType = method.getParameterTypes()[0].getTypeName();
        String toType = method.getParameterTypes()[1].getTypeName();
        return generateKey(fromType, toType);
    }

    private String generateKey(String... typeNames) {
        return StringUtils.joinWith(KEY_DELIMITER, typeNames);
    }

    private BaseConverter findConverter(Class<?> from, Class<?> to) {
        String key = generateKey(from.getTypeName(), to.getTypeName());
        BaseConverter foundConverter = mappedConverters.get(key);
        if (foundConverter != null) {
            return foundConverter;
        } else {
            throw new ConverterNotFoundException(String.format("Cannot find converter from %s to %s", from.getTypeName(), to.getTypeName()));
        }
    }
}
