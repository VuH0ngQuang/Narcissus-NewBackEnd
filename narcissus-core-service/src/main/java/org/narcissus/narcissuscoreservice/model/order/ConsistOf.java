package org.narcissus.narcissuscoreservice.model.order;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class ConsistOf {
    @Id
    private ConsistOfKey id;
    private int quantity;
}
