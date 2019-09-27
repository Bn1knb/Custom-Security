package com.epam.kamisarau.auth.repository.hibernate;

import com.epam.kamisarau.auth.repository.AuthRepository;

public class HbAuthRepositoryImpl implements AuthRepository {
    @Override
    public String getUserByTokenId(Long tokenId) {
        return null;
    }

    @Override
    public Long getTokenIdByUser(String username) {
        return null;
    }

    @Override
    public Long setTokenToUser(String username, Long tokenId) {
        return null;
    }

    @Override
    public void deleteTokenOfUser(String username) {

    }
}
