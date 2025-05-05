package org.narcissus.narcissuscoreservice.model.order;

import org.springframework.data.annotation.Id;

public class ConsistOf {
    @Id
    private ConsistOfKey id;
    private int quantity;
}
