package org.narcissus.narcissuscoreservice.model;

import java.util.Date;
import java.util.Objects;

public class UserEntity {
    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String email;
    private String password;
    //    private boolean admin;
//    @Column(name = "username")
    private String userName;
    private String phoneNumber;
    private String address;
    private Date date;

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
