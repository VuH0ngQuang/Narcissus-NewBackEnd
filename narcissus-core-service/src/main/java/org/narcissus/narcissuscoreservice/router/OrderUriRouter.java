package org.narcissus.narcissuscoreservice.router;

import java.util.Map;
import java.util.function.Function;

import org.narcissus.narcissuscoreservice.exceptions.ErrorHandler;
import org.narcissus.narcissuscoreservice.model.messagePayload.Message;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.model.order.Orders;
import org.narcissus.narcissuscoreservice.services.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderUriRouter {
    private static final Logger log = LoggerFactory.getLogger(OrderUriRouter.class);
    private final OrderService orderService;
    private final ErrorHandler errorHandler;
    private final ObjectMapper objectMapper;
    private final Map<String,Function<Message, ResponsePayload>> routers;

    @Autowired
    public OrderUriRouter(OrderService orderService, ErrorHandler errorHandler, ObjectMapper objectMapper,
            Map<String, Function<Message, ResponsePayload>> routers) {
        this.orderService = orderService;
        this.errorHandler = errorHandler;
        this.objectMapper = objectMapper;
        this.routers = Map.of(
            "api/v1/order/create",this::handleCreateOrder
        );
    }

    public ResponsePayload route(Message message) {
        String uri = message.getUri();
        Function<Message, ResponsePayload> handler = routers.get(uri);
        if (handler != null) {
            return handler.apply(message);
        } else return errorHandler.error("No route found", "route OrderUriRouter");
    }

    public ResponsePayload handleCreateOrder(Message message) {
        try {
            Orders request = Message.getPayload(objectMapper, message, Orders.class);
            return orderService.createOrder(request);
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "handleCreateOrder OrderUriRouter");
        }
    }
}
