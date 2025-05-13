package org.narcissus.narcissuscoreservice.handler;

import java.util.Collections;

import org.narcissus.narcissuscoreservice.config.AppConfig;
import org.narcissus.narcissuscoreservice.constants.MessageTypeEnum;
import org.narcissus.narcissuscoreservice.kafka.KafkaRequestHandler;
import org.narcissus.narcissuscoreservice.model.messagePayload.Message;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.router.OrderUriRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderHandler extends KafkaRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderHandler.class);
    private final OrderUriRouter orderUriRouter;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public OrderHandler(
        ObjectMapper objectMapper,
        AppConfig appConfig,
        OrderUriRouter orderUriRouter) {
        super(objectMapper, 
        appConfig.getKafka().getUrl(),
        appConfig.getKafka().getClusterId(),
        Collections.singletonList(appConfig.getChannels().getOrder()),
        1,null
        );
        this.orderUriRouter = orderUriRouter;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handle(Message message) {
        try {
            if (message.getMessageType().equals(MessageTypeEnum.REQUEST)) {
                log.info(objectMapper.writeValueAsString(message));
                ResponsePayload responsePayload  = orderUriRouter.route(message);
                sendResponse(responsePayload, message.getUri());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
}
