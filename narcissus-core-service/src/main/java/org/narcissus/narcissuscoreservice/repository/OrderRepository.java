package org.narcissus.narcissuscoreservice.repository;

import org.narcissus.narcissuscoreservice.model.order.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Orders, String>  {
    
}