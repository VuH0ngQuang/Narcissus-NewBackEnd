package org.narcissus.narcissuscoreservice.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.narcissus.narcissuscoreservice.constants.MessageTypeEnum;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

@Data
public class Message<T> {
    private MessageTypeEnum messageType;
    private String source;
    private String uri;
    private T payload;

    public static <E> E getData(ObjectMapper objectMapper, Message message, Class<E> clazz) throws IOException {
        return (E)objectMapper.readValue(objectMapper.writeValueAsBytes(message.getPayload()), clazz);
    }

    public static <E> E getData(ObjectMapper objectMapper, Message message, TypeReference<E> typeReference) throws IOException {
        return (E)objectMapper.readValue(objectMapper.writeValueAsBytes(message.getPayload()), typeReference);
    }

    public <E> E getData(ObjectMapper objectMapper, Class<E> clazz) throws IOException {
        return (E)getData(objectMapper, this, clazz);
    }

    public <E> E getData(ObjectMapper objectMapper, TypeReference<E> typeReference) throws IOException {
        return (E)getData(objectMapper, this, typeReference);
    }
}
