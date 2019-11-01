package com.bn1knb.newsblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@ApiModel(description = "All details about the Users. ")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated user's ID")
    private Long id;
    @ApiModelProperty(notes = "Unique username of a user")
    @Column(unique = true)
    private String username;
    @ApiModelProperty(notes = "Password of a user (must be at least 8 chars long)")
    private String password;
    @ApiModelProperty(notes = "First name of a user")
    private String firstName;
    @ApiModelProperty(notes = "Last name of a user")
    private String lastName;
    @ApiModelProperty(notes = "An email of a user")
    private String email;
    @CreatedDate
    @ApiModelProperty(notes = "The date user being created at")
    private Date createdAt;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ApiModelProperty(notes = "List of user's posts")
    List<Post> posts;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ApiModelProperty(notes = "List of user's comments")
    List<Comment> comments;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "User's state eg. ACTIVE, BANNED")
    private State state;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "User's auth role eg. ADMIN, USER, MODERATOR")
    private Role role;
    @OneToOne(orphanRemoval = true)
    @JoinColumn
    @JsonIgnore
    @ApiModelProperty(notes = "Token of user given by auth application located at 9090 port")
    private TokenModel token;

    public User(String username, String password, String firstName, String lastName, String email, Date createdAt, State state, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.state = state;
        this.role = role;
    }
}
