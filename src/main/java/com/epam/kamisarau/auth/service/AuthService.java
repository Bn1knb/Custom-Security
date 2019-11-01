package com.epam.kamisarau.auth.service;

import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;

public interface AuthService {
    UserModel loginByToken(TokenModel token);
    boolean isTokenNonExpired(TokenModel token);
    boolean isTokenPresent(String tokenValue);
    boolean isValid(TokenModel tokenModel);
    TokenModel updateToken(TokenModel tokenModel);
    TokenModel findTokenByTokenValue(String tokenValue);
    TokenModel createToken(String username);
    TokenModel getTokenOfUser(UserModel user);
    TokenModel setTokenToUser(UserModel user);
    void deleteTokenOfUser(UserModel user);
}
