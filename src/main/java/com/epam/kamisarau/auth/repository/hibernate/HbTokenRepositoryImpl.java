package com.epam.kamisarau.auth.repository.hibernate;

import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.repository.TokenRepository;

public class HbTokenRepositoryImpl implements TokenRepository {
    @Override
    public TokenModel store(TokenModel token) {
        return null;
    }

    @Override
    public TokenModel getToken(Long tokenId) {
        return null;
    }

    @Override
    public TokenModel updateToken(Long tokenId, TokenModel newToken) {
        return null;
    }

    @Override
    public TokenModel getTokenByValue(Long tokenValue) {
        return null;
    }

    @Override
    public void deleteTokenById(Long tokenId) {

    }
}
