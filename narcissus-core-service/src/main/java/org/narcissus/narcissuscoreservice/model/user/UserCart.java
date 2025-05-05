package org.narcissus.narcissuscoreservice.model.user;

import lombok.Data;

@Data
public class UserCart {
    private String productId;
    private int quantity;
}
