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
@Table(name = "USER_STORAGE")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@Column(unique = true)
    private String username;
    private String password;
    private Date registeredAt;
    private String name;
    private String surname;
    private boolean isActive;
    @OneToOne
    @JoinColumn
    private RoleModel role;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn
    private TokenModel token;
}
