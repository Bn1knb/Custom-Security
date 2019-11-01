package com.epam.kamisarau.auth.repository;

import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;

public interface TokenRepository {
    TokenModel store(TokenModel token);
    TokenModel getToken(Long tokenId);
    TokenModel updateToken(Long tokenId, TokenModel newToken);
    TokenModel getTokenByValue(String tokenValue);
    void deleteTokenById(Long tokenId);
    void deleteTokenOfUser(UserModel user);
}
