package org.narcissus.narcissuscoreservice.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.narcissus.narcissuscoreservice.constants.MessageTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

@Data
public class Message<T> {
    private MessageTypeEnum messageType;
    private String source;
    private String uri;
    @Getter(AccessLevel.NONE)
    private T payload;

    public static <E> E getPayload(ObjectMapper objectMapper, Message message, Class<E> clazz) throws IOException {
        return (E)objectMapper.readValue(objectMapper.writeValueAsBytes(message.getPayload()), clazz);
    }

    public static <E> E getPayload(ObjectMapper objectMapper, Message message, TypeReference<E> typeReference) throws IOException {
        return (E)objectMapper.readValue(objectMapper.writeValueAsBytes(message.getPayload()), typeReference);
    }

    public <E> E getPayload(ObjectMapper objectMapper, Class<E> clazz) throws IOException {
        return (E)getPayload(objectMapper, this, clazz);
    }

    public <E> E getPayload(ObjectMapper objectMapper, TypeReference<E> typeReference) throws IOException {
        return (E)getPayload(objectMapper, this, typeReference);
    }

    public T getPayload() { return this.payload;}
}
