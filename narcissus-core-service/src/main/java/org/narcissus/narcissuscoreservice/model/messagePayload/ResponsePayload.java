package org.narcissus.narcissuscoreservice.model.messagePayload;

import lombok.Builder;
import lombok.Data;
import org.narcissus.narcissuscoreservice.constants.MessageStatusEnum;

@Data
@Builder
public class ResponsePayload {
    private MessageStatusEnum messageStatusEnum;
    private String errorMessage;
    private Object payload;
}
