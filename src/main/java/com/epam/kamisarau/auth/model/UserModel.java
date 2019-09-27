package com.epam.kamisarau.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Date registeredAt;
    private String name;
    private String surname;
    private boolean isActive;
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_name"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private RoleModel role;
    @JoinTable(name = "user_token", joinColumns = @JoinColumn(name = "user_name"), inverseJoinColumns = @JoinColumn(name = "token_id"))
    private TokenModel token;
}
