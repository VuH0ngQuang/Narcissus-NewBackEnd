package org.narcissus.narcissuscoreservice.model.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConsistOfKey implements Serializable {
    private String  ordersId;
    private String  productId;
}
