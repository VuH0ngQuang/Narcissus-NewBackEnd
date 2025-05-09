package org.narcissus.narcissuscoreservice.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.narcissus.narcissuscoreservice.exceptions.ErrorHandler;
import org.narcissus.narcissuscoreservice.model.messagePayload.Message;
import org.narcissus.narcissuscoreservice.model.user.UserEntity;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.services.userEntity.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class UserUriRouter {
    private static final Logger log = LoggerFactory.getLogger(UserUriRouter.class);
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ErrorHandler errorHandler;
    private final Map<String, Function<Message, ResponsePayload>> routers;

    @Autowired
    public UserUriRouter(UserService userService, ObjectMapper objectMapper, ErrorHandler errorHandler) {
        this.userService = userService;
        this.errorHandler = errorHandler;
        this.objectMapper = objectMapper;
        this.routers = Map.of(
                "/api/v1/user/createUser", this::handleCreateUser,
                "/api/v1/user/updateUser", this::handleUpdateUser,
                "/api/v1/user/addToCart", this::handleAddToCart,
                "/api/v1/user/removeFromCart", this::handleRemoveFromCart,
                "/api/v1/user/removeAllFromCart", this::handleRemoveAllFromCart
        );
    }

    public ResponsePayload route(Message message) {
        String uri = message.getUri();
        Function<Message, ResponsePayload> handler = routers.get(uri);
        if (handler != null) {
            return handler.apply(message);
        } else {
            return errorHandler.error("No route found", "route UserUriRouter");
        }
    }

    public ResponsePayload handleAddToCart(Message message) {
        try {
            UserEntity request = Message.getPayload(objectMapper, message, UserEntity.class);
            return userService.addToCart(request);
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "handleAddToCart UserUriRouter");
        }
    }

    public ResponsePayload handleRemoveFromCart(Message message) {
        try {
            UserEntity request = Message.getPayload(objectMapper, message, UserEntity.class);
            return userService.removeCart(request);
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "handleRemoveFromCart UserUriRouter");
        }
    }

    public ResponsePayload handleRemoveAllFromCart(Message message) {
        try {
            UserEntity request = Message.getPayload(objectMapper, message, UserEntity.class);
            return userService.removeAllCart(request);
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "handleRemoveAllFromCart UserUriRouter");
        }
    }

    public ResponsePayload handleUpdateUser(Message message) {
        try {
            UserEntity request = Message.getPayload(objectMapper, message, UserEntity.class);
            return userService.updateUser(request);
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "handleUpdateUser UserUriRouter");
        }
    }

    private ResponsePayload handleCreateUser(Message message) {
        try {
            UserEntity request = Message.getPayload(objectMapper, message, UserEntity.class);
            return userService.createUser(request);
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "handelCreateUser UserUriRouter");
        }
    }
}