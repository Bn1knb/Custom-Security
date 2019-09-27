package com.epam.kamisarau.auth.service;

import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.TokenValueDto;

public interface AuthService {
    UserModel loginByToken(TokenModel token);
    boolean isTokenNonExpired(TokenModel token);
    boolean isTokenPresent(Long tokenValue);
    boolean isValid(TokenModel tokenModel);
    TokenModel updateToken(TokenModel tokenModel);
    TokenModel findTokenByTokenValue(Long tokenValue);
    TokenModel createToken();
    TokenModel getTokenOfUser(UserModel user);
    TokenValueDto setTokenToUser(UserModel user, TokenModel token);
    void deleteTokenOfUser(UserModel user);
}
