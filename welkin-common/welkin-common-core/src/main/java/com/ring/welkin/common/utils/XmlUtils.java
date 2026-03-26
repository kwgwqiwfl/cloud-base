package com.ring.welkin.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper.Builder;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class XmlUtils {

    private static final XmlMapper mapper = buildXmlMapper(false);

    private XmlUtils() {
    }

    public static XmlMapper buildXmlMapper(boolean pretty) {
        Builder builder = XmlMapper.builder();
        if (pretty) {
            builder.enable(SerializationFeature.INDENT_OUTPUT);
        }
        builder.addModule(new JavaTimeModule());
        builder.serializationInclusion(Include.NON_NULL);
        builder.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 设置转换模式
        builder.enable(MapperFeature.USE_STD_BEAN_NAMING);
        return builder.build();
    }

    @SuppressWarnings("rawtypes")
    public static <C extends Collection, V> JavaType contructCollectionType(Class<C> collectionClass,
                                                                            Class<V> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    @SuppressWarnings("rawtypes")
    public static <M extends Map, K, V> JavaType contructMapType(Class<M> mapClass, Class<K> keyClass,
                                                                 Class<V> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    public static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public static <M extends Map<K, V>, K, V> M fromXml(String src, Class<M> mCkass, Class<K> kClass, Class<V> vClass) {
        return fromXml(src, contructMapType(mCkass, kClass, vClass));
    }

    public static <M extends Map<K, V>, K, V> M fromObject(Object src, Class<M> mCkass, Class<K> kClass,
                                                           Class<V> vClass) {
        return fromObject(src, contructMapType(mCkass, kClass, vClass));
    }

    public static <C extends Collection<O>, O> C fromXml(String src, Class<C> cClass, Class<O> oClass) {
        return fromXml(src, contructCollectionType(cClass, oClass));
    }

    public static <C extends Collection<O>, O> C fromObject(Object src, Class<C> cClass, Class<O> oClass) {
        return fromObject(src, contructCollectionType(cClass, oClass));
    }

    public static <T> T fromXml(String src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(String src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(String src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(URL src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(URL src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(URL src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(File src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(File src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(File src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(Reader src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(Reader src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(Reader src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(InputStream src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(InputStream src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(InputStream src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(byte[] src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(byte[] src, int offset, int len, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, offset, len, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(byte[] src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(byte[] src, int offset, int len, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, offset, len, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(byte[] src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(byte[] src, int offset, int len, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, offset, len, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(DataInput src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromXml(DataInput src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromObject(Object src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.convertValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromObject(Object src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.convertValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromObject(Object src, JavaType javaType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.convertValue(src, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(File dest, Object value) {
        Assert.notNull(dest, "dest should not be null");
        try {
            mapper.writeValue(dest, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(OutputStream dest, Object value) {
        Assert.notNull(dest, "dest should not be null");
        try {
            mapper.writeValue(dest, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(DataOutput dest, Object value) {
        Assert.notNull(dest, "dest should not be null");
        try {
            mapper.writeValue(dest, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toXml(Object value) {
        return toXml(value, false);
    }

    public static String toXml(Object value, boolean pretty) {
        try {
            if (pretty) {
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
            }
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pretty) {
                mapper.disable(SerializationFeature.INDENT_OUTPUT);
            }
        }
    }

    public static byte[] toByte(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String xml = XmlUtils.toXml(new User("lisi", 1234), true);
        System.out.println(xml);
        User user = XmlUtils.fromXml(xml, User.class);
        System.out.println(user);
        User user1 = XmlUtils.fromObject(user, User.class);
        System.out.println(user1);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
    }
}
