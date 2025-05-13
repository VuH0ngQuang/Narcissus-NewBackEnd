package org.narcissus.narcissuscoreservice.services.redis;

import org.narcissus.narcissuscoreservice.config.AppConfig;
import org.narcissus.narcissuscoreservice.constants.RedisKeyEnum;
import org.narcissus.narcissuscoreservice.daos.RedisDao;
import org.narcissus.narcissuscoreservice.model.order.Orders;
import org.narcissus.narcissuscoreservice.model.product.Product;
import org.narcissus.narcissuscoreservice.model.user.UserEntity;
import org.narcissus.narcissuscoreservice.repository.OrderRepository;
import org.narcissus.narcissuscoreservice.repository.ProductRepository;
import org.narcissus.narcissuscoreservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Service
public class RedisService {
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    private final AppConfig appConfig;
    private final RedisDao redisDao;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ObjectMapper  objectMapper;
    
    @Autowired
    public RedisService(AppConfig appConfig, RedisDao redisDao, OrderRepository orderRepository,
            ProductRepository productRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.redisDao = redisDao;
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init(){

    }

    public void saveOrder(String id, Object data) {
        redisDao.save(RedisKeyEnum.ORDERS.toString(), id, data);
    }

    public void saveProduct(String id, Object data) {
        redisDao.save(RedisKeyEnum.PRODUCT.toString(), id, data);
    }

    public void saveUser(String id, Object data) {
        redisDao.save(RedisKeyEnum.USERS.toString(), id, data);
        String email = objectMapper.valueToTree(data).get("email").asText();
        redisDao.save(RedisKeyEnum.EMAIL_USER.toString(), email, id);
    }

    public String getUserId(String email) {
        return redisDao.get(RedisKeyEnum.EMAIL_USER.toString(), email, String.class);
    }

    public Orders getOrder(String id) {
        Orders orders = redisDao.get(RedisKeyEnum.ORDERS.toString(), id, Orders.class);
        if (orders != null) {
            return orders;
        } else return orderRepository.findById(id).orElse(null);
    }
    
    public Product getProduct(String id) {
        Product product = redisDao.get(RedisKeyEnum.PRODUCT.toString(), id, Product.class);
        if (product != null) {
            return product;
        } else return productRepository.findById(id).orElse(null);
    }

    public UserEntity getUser(String id) {
        UserEntity user = redisDao.get(RedisKeyEnum.USERS.toString(), id, UserEntity.class);
        if (user != null) {
            return user;
        } else return userRepository.findById(id).orElse(null);
    }
}
