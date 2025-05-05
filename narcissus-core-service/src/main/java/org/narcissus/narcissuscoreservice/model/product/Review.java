package org.narcissus.narcissuscoreservice.model.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "reviews")
public class Review {
    @Id
    private ReviewKey id;
    int stars;
    String content;
    Date date;
}
