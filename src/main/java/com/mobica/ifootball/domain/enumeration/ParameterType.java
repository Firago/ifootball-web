package com.mobica.ifootball.domain.enumeration;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The ParameterType enumeration.
 */
public enum ParameterType {

    STRING(String.class),
    DATE(ZonedDateTime.class),
    DECIMAL(Integer.class),
    FLOAT(Float.class),
    BOOLEAN(Boolean.class);

    private final Class type;


    public Class getType() {
        return type;
    }

    private ParameterType(Class type) {
        this.type = type;
    }

    private static interface ValueFormatter<T> {

        T fromString(String value);

        String toString(T value);
    }

    private static final Map<Class<?>, ValueFormatter> formatterByClazz = new HashMap<>();

    static {

        formatterByClazz.put(String.class, new ValueFormatter<String>() {
            @Override
            public String fromString(String value) {
                return value;
            }

            @Override
            public String toString(String value) {
                return value;
            }
        });

        formatterByClazz.put(Date.class, new ValueFormatter<ZonedDateTime>() {
            @Override
            public ZonedDateTime fromString(String value) {
                return ZonedDateTime.parse(value);
            }

            @Override
            public String toString(ZonedDateTime value) {
                return value.toString();
            }
        });

        formatterByClazz.put(Integer.class, new ValueFormatter<Integer>() {
            @Override
            public Integer fromString(String value) {
                return Integer.parseInt(value);
            }

            @Override
            public String toString(Integer value) {
                return Integer.toString(value);
            }
        });

        formatterByClazz.put(Float.class, new ValueFormatter<Float>() {
            @Override
            public Float fromString(String value) {
                return Float.parseFloat(value);
            }

            @Override
            public String toString(Float value) {
                return Float.toString(value);
            }
        });

        formatterByClazz.put(Boolean.class, new ValueFormatter<Boolean>() {
            @Override
            public Boolean fromString(String value) {
                return Boolean.parseBoolean(value);
            }

            @Override
            public String toString(Boolean value) {
                return Boolean.toString(value);
            }
        });

    }

    public <T> T valueFromString(String string) {
        if (string == null || string.isEmpty()) {
            return this.getType().equals(String.class) ? (T) string : null;
        }
        ValueFormatter<T> formatter = formatterByClazz.get(type);
        if (formatter == null) {
            throw new IllegalStateException("Can not find formatter for: " + type.getName());
        }
        return formatter.fromString(string);
    }

    public <T> String valueToString(T value) {
        if (value == null) {
            return STRING.equals(this) ? "" : null;
        }
        ValueFormatter<T> formatter = formatterByClazz.get(type);
        if (formatter == null) {
            throw new IllegalStateException("Can not find formatter for: " + type.getName());
        }
        return formatter.toString(value);

    }
}
