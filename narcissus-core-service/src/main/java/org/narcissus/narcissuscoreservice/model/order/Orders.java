package org.narcissus.narcissuscoreservice.model.order;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "orders")
public class Orders {
    @Id
    private String ordersId;
    private long money;
    private boolean shipped;
    private String address;
    private String status;
    private Date date;
    private String cancellationReason;
    private String canceledAt;
    private String transactionDateTime;

    private String userId;

    private List<ConsistOf> consistOfList;
}
