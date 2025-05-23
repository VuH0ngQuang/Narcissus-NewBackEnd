package org.narcissus.narcissuscoreservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.narcissus.narcissuscoreservice.config.AppConfig;
import org.narcissus.narcissuscoreservice.constants.MessageTypeEnum;
import org.narcissus.narcissuscoreservice.model.messagePayload.Message;
import org.narcissus.narcissuscoreservice.kafka.KafkaRequestHandler;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.router.UserUriRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserHandler extends KafkaRequestHandler {

    private final static Logger log = LoggerFactory.getLogger(UserHandler.class);
    private final UserUriRouter userUriRouter;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserHandler(
            ObjectMapper objectMapper,
            AppConfig appConfig,
            UserUriRouter userUriRouter){
        super(objectMapper,
                appConfig.getKafka().getUrl(),
                appConfig.getKafka().getClusterId(),
                Collections.singletonList(appConfig.getChannels().getUser()),
                1, null
        );
        this.userUriRouter = userUriRouter;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handle(Message message) {
        try {
            if (message.getMessageType() == MessageTypeEnum.REQUEST) {
                log.info(objectMapper.writeValueAsString(message));
                ResponsePayload responsePayload = userUriRouter.route(message);
                sendResponse(responsePayload, message.getUri());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
