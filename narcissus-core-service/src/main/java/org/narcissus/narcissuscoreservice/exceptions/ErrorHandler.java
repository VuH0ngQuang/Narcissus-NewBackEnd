package org.narcissus.narcissuscoreservice.exceptions;

import org.narcissus.narcissuscoreservice.constants.MessageStatusEnum;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {
    public ResponsePayload error(String error, String function) {
        return ResponsePayload.builder().messageStatusEnum(MessageStatusEnum.ERROR).errorMessage(String.format("Error %s: %s", function, error)).build();
    }
}
