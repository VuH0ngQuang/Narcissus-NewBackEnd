package org.narcissus.narcissuscoreservice.model.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String productId;
    private String productName;
    private String productInfo;
    private String productType;
    private int productStockQuantity;
    private long productPrice;
    private Date productDate;
    private byte[] productImage;
}
