package dev.article.article.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_Id")
    private String userId;
    @Column(name = "user_Name")
    private String userName;
    @Column(name = "user_Password")
    private String userPassword;
    @Column(name = "user_Access_Lvl")
    private int userAccessLvl;

    public UserEntity() {
        // Default constructor
    }

    public void setName(String userName) {
        this.userName = userName;
    }

}
