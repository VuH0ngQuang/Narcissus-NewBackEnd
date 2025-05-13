package org.narcissus.narcissuscoreservice.daos;

import java.io.IOException;

import org.narcissus.narcissuscoreservice.constants.RedisDataTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisDao {
    private static final Logger log = LoggerFactory.getLogger(RedisDao.class);
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public RedisDao(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void save(String key, String id, Object data) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        try {
            hashOperations.put(key, id, objectToString(data));
        } catch (Exception e) {
            log.error("Error while save to redis: {}_{}_{}",key,id,data);
        }
    }

    public <E> E get(String key, String id, Class<E> clazz) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        try {
            String data = hashOperations.get(key, id);
            return (E) StringToObject(data, clazz);
        } catch (Exception e) {
            log.error("Error while save to redis: {}_{}",key,id);
            return null;
        }
    }

    private <E> String objectToString(E data) throws JsonProcessingException{
        if (data == null) {
            return RedisDataTypeEnum.NULL.getType() + data;
        } else if (data instanceof Boolean ) {
            return RedisDataTypeEnum.BOOLEAN.getType() + data;
        } else if (data instanceof String) {
            return RedisDataTypeEnum.STRING.getType() + data;
        } else if (data instanceof Number) {
            return RedisDataTypeEnum.NUMBER.getType() + data;
        } else {
            return RedisDataTypeEnum.OBJECT.getType() + objectMapper.writeValueAsString(data);
        }
    }

    private <E> E StringToObject(String data, Class<E> clazz) throws IOException {
        String type = data.substring(0, 1);
        if (type.equals(RedisDataTypeEnum.NULL.getType())) {
            return null;
        }
        String value = data.substring(1);
        if (type.equals(RedisDataTypeEnum.BOOLEAN.getType())) {
            return (E) Boolean.valueOf(value);
        } else if (type.equals(RedisDataTypeEnum.STRING.getType())) {
            return (E) value;
        } else if (type.equals(RedisDataTypeEnum.NUMBER.getType())) {
            return (E) Double.valueOf(value);
        } else {
            return objectMapper.readValue(value, clazz);
        }
    }
}
