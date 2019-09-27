package com.epam.kamisarau.auth.repository;

public interface AuthRepository {
    String getUserByTokenId(Long tokenId);
    Long getTokenIdByUser(String username);
    Long setTokenToUser(String username, Long tokenId);
    void deleteTokenOfUser(String username);
}
