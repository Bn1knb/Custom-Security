package com.epam.kamisarau.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "user")
@ApiModel(description = "All details about the Users. ")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated user ID")
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(name = "created_at")
    private Date registeredAt;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private RoleModel role;
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private State state;
    @OneToOne(orphanRemoval = true)
    @JsonIgnore
    @JoinColumn
    private TokenModel token;
}
