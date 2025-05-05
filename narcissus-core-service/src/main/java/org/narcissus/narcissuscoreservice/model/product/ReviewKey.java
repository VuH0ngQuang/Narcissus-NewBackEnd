package org.narcissus.narcissuscoreservice.model.product;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReviewKey implements Serializable {
    private String productId;
    private String userId;
}
