package org.narcissus.narcissuscoreservice.model.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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

    private Set<UserCart> userCarts;

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date=" + date +
                '}';
    }
}
