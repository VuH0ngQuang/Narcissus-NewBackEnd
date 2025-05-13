package org.narcissus.narcissuscoreservice.services.userEntity;

import org.narcissus.narcissuscoreservice.constants.MessageStatusEnum;
import org.narcissus.narcissuscoreservice.exceptions.ErrorHandler;
import org.narcissus.narcissuscoreservice.model.user.UserCart;
import org.narcissus.narcissuscoreservice.model.user.UserEntity;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.services.redis.RedisService;
import org.narcissus.narcissuscoreservice.utils.NullFieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final ErrorHandler errorHandler;

    @Autowired
    public UserService(RedisService redisService, PasswordEncoder passwordEncoder, ErrorHandler errorHandler) {
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
        this.errorHandler = errorHandler;
    }

    public ResponsePayload createUser (UserEntity request) {
        try {
            if (redisService.getUserId(request.getEmail()) != null) return errorHandler.error("Email already used","createUser USERSERVICE");
            request.setDate(new Date());
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            
            return save(request, "createUser USERSERVICE");
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(),"createUser USERSERVICE");
        }
    }

    public ResponsePayload updateUser (UserEntity request) {
        try {
            UserEntity user = findUser(request.getUserId());
            if (user !=null) {
                if (request.getPassword()!=null) user.setPassword(passwordEncoder.encode(request.getPassword()));
                if (request.getUserName()!=null) user.setUserName(request.getUserName());
                if (request.getPhoneNumber()!=null) user.setPhoneNumber(request.getPhoneNumber());
                if (request.getAddress()!=null) user.setAddress(request.getAddress());
                return save(user, "updateUser USERSERVICE");
            } else return errorHandler.error("Cannot find user with ID: "+request.getUserId(),"updateUser USERSERVICE");
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "updateUser USERSERVICE");
        }
    }

    public ResponsePayload addToCart(UserEntity request) {
        try {
            UserEntity user = findUser(request.getUserId());
            if (user !=null) {
                Set<UserCart> carts = user.getUserCarts();
                request.getUserCarts().parallelStream().forEach(carts::add);
                return save(user, "addToCart USERSERVICE");
            } else return errorHandler.error("Cannot find user with ID: "+request.getUserId(),"addToCart USERSERVICE");
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "addToCart USERSERVICE");
        }
    }

    public ResponsePayload removeAllCart(UserEntity request) {
        try {
            UserEntity user = findUser(request.getUserId());
            if (user != null) {
                user.getUserCarts().clear();
                return save(user, "updateUser USERSERVICE");
            } else return errorHandler.error("Cannot find user with ID: "+request.getUserId(),"removeAllCart USERSERVICE");
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "updateUser USERSERVICE");
        }
    }

    public ResponsePayload removeCart(UserEntity request) {
        UserEntity user = findUser(request.getUserId());
        if (user != null) {
            user.getUserCarts().removeAll(request.getUserCarts());
            return save(user, "removeCart USERSERVICE");
        } else return errorHandler.error("Cannot find user with ID: "+request.getUserId(),"removeCart USERSERVICE");
    }

    private UserEntity findUser(String userId) {return redisService.getUser(userId);}

    private ResponsePayload save(UserEntity user, String function) {
       List<String> nullList = NullFieldChecker.check(user,  "userCarts","userId");
       if (nullList.isEmpty()) {
        redisService.saveUser(user.getUserId(), user);
        return ResponsePayload.builder().messageStatusEnum(MessageStatusEnum.OK).build();
       } else return errorHandler.error(nullList.toString()+"is null", function);
    }
}
