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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long tokenValue;
    private Date expirationDate;
}
