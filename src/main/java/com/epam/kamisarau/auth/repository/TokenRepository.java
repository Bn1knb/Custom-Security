package com.epam.kamisarau.auth.repository;

import com.epam.kamisarau.auth.model.TokenModel;

public interface TokenRepository {
    TokenModel store(TokenModel token);
    TokenModel getToken(Long tokenId);
    TokenModel updateToken(Long tokenId, TokenModel newToken);
    TokenModel getTokenByValue(Long tokenValue);
    void deleteTokenById(Long tokenId);
}
