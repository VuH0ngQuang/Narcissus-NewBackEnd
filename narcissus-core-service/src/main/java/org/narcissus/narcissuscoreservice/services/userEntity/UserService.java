package org.narcissus.narcissuscoreservice.services.userEntity;

import org.narcissus.narcissuscoreservice.constants.MessageStatusEnum;
import org.narcissus.narcissuscoreservice.exceptions.ErrorHandler;
import org.narcissus.narcissuscoreservice.model.user.UserEntity;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
import org.narcissus.narcissuscoreservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ErrorHandler errorHandler;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ErrorHandler errorHandler) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.errorHandler = errorHandler;
    }

    public ResponsePayload createUser (UserEntity request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) return errorHandler.error("Email already used","createUser USERSERVICE");

            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(request.getEmail());
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            userEntity.setUserName(request.getUserName());
            userEntity.setPhoneNumber(request.getPhoneNumber());
            userEntity.setAddress(request.getAddress());
            userEntity.setDate(new Date());

            userRepository.save(userEntity);
            return ResponsePayload.builder().messageStatusEnum(MessageStatusEnum.OK).build();
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(),"createUser USERSERVICE");
        }
    }

    public ResponsePayload updateUser (UserEntity request) {
        try {
            UserEntity user = userRepository.findById(request.getUserId()).orElse(null);
            if (user == null) errorHandler.error("Not found user with ID: " + request.getUserId(), "updateUser USERSERVICE");

            if (!request.getEmail().equals(user.getEmail())) user.setEmail(request.getEmail());
            if (!request.getPassword().equals(user.getPassword())) user.setPassword(passwordEncoder.encode(request.getPassword()));
            if (!request.getUserName().equals(user.getUserName())) user.setUserName(request.getUserName());
            if (!request.getPhoneNumber().equals(user.getPhoneNumber())) user.setPhoneNumber(request.getPhoneNumber());
            if (!request.getAddress().equals(user.getAddress())) user.setAddress(request.getAddress());

            userRepository.save(user);
            return ResponsePayload.builder().messageStatusEnum(MessageStatusEnum.OK).build();
        } catch (Exception e) {
            return errorHandler.error(e.getMessage(), "updateUser USERSERVICE");
        }
    }
}
