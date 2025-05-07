package org.narcissus.narcissuscoreservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.narcissus.narcissuscoreservice.config.AppConfig;
import org.narcissus.narcissuscoreservice.constants.MessageTypeEnum;
import org.narcissus.narcissuscoreservice.kafka.KafkaRequestHandler;
import org.narcissus.narcissuscoreservice.model.Message;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.router.ProductUriRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ProductHandler extends KafkaRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductHandler.class);
    private final ProductUriRouter productUriRouter;

    @Autowired
    public ProductHandler(
            ObjectMapper objectMapper,
            AppConfig appConfig,
            ProductUriRouter productUriRouter) {
        super(objectMapper,
                appConfig.getKafka().getUrl(),
                appConfig.getKafka().getClusterId(),
                Collections.singletonList(appConfig.getChannels().getProduct()),
                1, null
        );
        this.productUriRouter = productUriRouter;
    }

    @Override
    protected void handle(Message message) {
        if (message.getMessageType() == MessageTypeEnum.REQUEST) {
            ResponsePayload responsePayload = productUriRouter.route(message);
            sendResponse(responsePayload, message.getUri());
        }
    }
}
