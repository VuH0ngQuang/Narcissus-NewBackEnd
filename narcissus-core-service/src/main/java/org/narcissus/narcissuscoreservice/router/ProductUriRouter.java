package org.narcissus.narcissuscoreservice.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.narcissus.narcissuscoreservice.exceptions.ErrorHandler;
import org.narcissus.narcissuscoreservice.model.Message;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.model.product.Product;
import org.narcissus.narcissuscoreservice.services.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class ProductUriRouter {
    private static final Logger log = LoggerFactory.getLogger(ProductUriRouter.class);
    private final ProductService productService;
    private final ErrorHandler errorHandler;
    private final ObjectMapper objectMapper;
    private final Map<String, Function<Message, ResponsePayload>> routers;

    @Autowired
    public ProductUriRouter(ProductService productService, ObjectMapper objectMapper,ErrorHandler errorHandler) {
        this.productService = productService;
        this.objectMapper = objectMapper;
        this.errorHandler = errorHandler;
        this.routers = Map.of(
                "api/v1/product/createProduct", this::handleCreateProduct,
                "api/v1/product/updateProduct", this::handleUpdateProduct
        );
    }

    public ResponsePayload handleCreateProduct(Message message) {
        try {
            Product request = Message.getPayload(objectMapper, message, Product.class);
            return productService.createProduct(request);
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "handleCreateProduct ProductUriRouter");
        }
    }

    public ResponsePayload handleUpdateProduct(Message message) {
        try {
            Product request = Message.getPayload(objectMapper, message, Product.class);
            return productService.updateProduct(request);
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "handleUpdateProduct ProductUriRouter");
        }
    }
}
