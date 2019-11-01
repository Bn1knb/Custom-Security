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
@Table(name = "TOKEN_STORAGE")
public class TokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token_value")
    private String tokenValue;
    @Column(name = "expiration_date")
    private Date expirationDate;
}
