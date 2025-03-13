package com.hamidspecial.medihive.util;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hamidspecial.medihive.exception.DataSerializerException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ObjectSerializer {

    private static final Logger LOGGER  = (Logger) LoggerFactory.getLogger(ObjectSerializer.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    private ObjectSerializer(){}

    public static <T> String serializeToJson(T object) throws DataSerializerException {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Error occurred while serializing to JSON. Error ==> {}", ex.getMessage(), ex);
            throw new DataSerializerException("Error serializing to JSON", ex);
        }
    }

    public static <T> T deserializeFromJson(String json, Class<T> objectType) throws DataSerializerException {
        try {
            return OBJECT_MAPPER.readValue(json, objectType);
        } catch (IOException ex) {
            LOGGER.error("Error occurred while deserializing JSON to object. Error ==> {}", ex.getMessage(), ex);
            throw new DataSerializerException("Error deserializing JSON to object", ex);
        }
    }

    public static <T, L extends List<T>> L deserializeFromJson(String json, TypeReference<L> typeReference) throws DataSerializerException {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException ex) {
            LOGGER.error("Error occurred while deserializing JSON to object. Error ==> {}", ex.getMessage(), ex);
            throw new DataSerializerException("Error deserializing JSON to object", ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, Object typeInfo) throws DataSerializerException {
        try {
            return switch (typeInfo) {
                case Class<?> clazz -> (T) OBJECT_MAPPER.readValue(json, clazz);
                case TypeReference<?> typeRef -> (T) OBJECT_MAPPER.readValue(json, typeRef);
                case JavaType javaType -> (T) OBJECT_MAPPER.readValue(json, javaType);
                case null -> throw new IllegalArgumentException("Type information cannot be null");
                default -> throw new IllegalArgumentException("Unsupported type information: " + typeInfo.getClass());
            };
        } catch (IOException ex) {
            LOGGER.error("Error occurred while deserializing. Error ==> {}", ex.getMessage(), ex);
            throw new DataSerializerException("Error deserializing", ex);
        }
    }

    public static <T> T deserializeFromStream(InputStream inputStream, Class<T> objectType) throws DataSerializerException {
        try (inputStream) {
            return OBJECT_MAPPER.readValue(inputStream, objectType);
        } catch (IOException ex) {
            LOGGER.error("Error occurred while deserializing stream to object. Error ==> {}", ex.getMessage(), ex);
            throw new DataSerializerException("Error deserializing stream to object", ex);
        }
    }
}