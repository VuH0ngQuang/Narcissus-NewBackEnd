package org.narcissus.narcissuscoreservice.services.product;

import java.util.Date;
import java.util.List;

import org.narcissus.narcissuscoreservice.constants.MessageStatusEnum;
import org.narcissus.narcissuscoreservice.exceptions.ErrorHandler;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.model.product.Product;
import org.narcissus.narcissuscoreservice.repository.ProductRepository;
import org.narcissus.narcissuscoreservice.utils.NullFieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private ErrorHandler errorHandler;

    @Autowired
    public ProductService(ProductRepository productRepository, ErrorHandler errorHandler) {
        this.productRepository = productRepository;
        this.errorHandler = errorHandler;
    }

    public ResponsePayload createProduct(Product request) {
        try {
            request.setProductDate(new Date());
            
            return save(request, "createProduct ProductService");
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "createProduct ProductService");
        }
    }

    public ResponsePayload updateProduct(Product request) {
        try {
            Product product = findProduct(request.getProductId());
            if (product != null) {
                if (request.getProductName()!=null) product.setProductName(request.getProductName());
                if (request.getProductInfo()!=null) product.setProductInfo(request.getProductInfo());
                if (request.getProductType()!=null) product.setProductType(request.getProductType());
                if (request.getProductStockQuantity() > 0) product.setProductStockQuantity(request.getProductStockQuantity());
                if (request.getProductPrice() > 0) product.setProductPrice(request.getProductPrice());
                if (request.getProductImage() != null) product.setProductImage(request.getProductImage());
                return save(product," updateProduct ProductService");
            } else return errorHandler.error("Cannot find product with ID: "+request.getProductId(),"updateProduct ProductService");
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "updateProduct ProductService");
        }
    }

    private Product findProduct(String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    private ResponsePayload save(Product product, String function ) {
        List<String> nullList = NullFieldChecker.check(product, "productImage", "productId");
        if (nullList.isEmpty()) {
            productRepository.save(product);
            return ResponsePayload.builder().messageStatusEnum(MessageStatusEnum.OK).build();
        } else return errorHandler.error(nullList.toString()+"is null", function);
    }
}