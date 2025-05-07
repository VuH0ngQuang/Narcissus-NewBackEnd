package org.narcissus.narcissuscoreservice.repository;

import org.narcissus.narcissuscoreservice.model.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {
}
