package com.epam.kamisarau.auth.model.dto;

import com.epam.kamisarau.auth.model.TokenModel;
import lombok.Data;

import java.io.Serializable;

@Data
public class TokenValueDto implements Serializable {
    private Long tokenValue;

    public TokenValueDto getTokenValueFromToken(TokenModel token) {
        this.tokenValue = token.getTokenValue();
        return this;
    }
}
