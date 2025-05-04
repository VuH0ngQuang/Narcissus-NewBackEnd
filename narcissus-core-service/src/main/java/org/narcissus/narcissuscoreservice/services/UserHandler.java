package org.narcissus.narcissuscoreservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.narcissus.narcissuscoreservice.config.AppConfig;
import org.narcissus.narcissuscoreservice.model.Message;
import org.narcissus.narcissuscoreservice.kafka.KafkaRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserHandler extends KafkaRequestHandler {

    private final static Logger log = LoggerFactory.getLogger(UserHandler.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public UserHandler(
            ObjectMapper objectMapper,
            AppConfig appConfig
    ){
        super(objectMapper,
                appConfig.getKafka().getUrl(),
                appConfig.getKafka().getClusterId(),
                Collections.singletonList(appConfig.getChannels().getUser()),
                1, null
        );
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handle(Message message) {
         log.warn(message.toString());
    }
}
