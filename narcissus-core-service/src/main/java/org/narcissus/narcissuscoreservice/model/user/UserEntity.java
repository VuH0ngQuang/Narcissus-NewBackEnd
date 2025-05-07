package org.narcissus.narcissuscoreservice.model.user;

import lombok.Data;
import org.narcissus.narcissuscoreservice.constants.RoleEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@Data
@Document(collection = "users")
public class UserEntity {
    @Id
    private String userId;
    private String email;
    private String password;
    //    private boolean admin;
    private String userName;
    private String phoneNumber;
    private String address;
    private Date date;

    private RoleEnum role;

    private Set<UserCart> userCarts;
}
