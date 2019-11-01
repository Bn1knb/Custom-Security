package com.epam.kamisarau.auth.repository;

import com.epam.kamisarau.auth.model.UserModel;

public interface UserRepository {
    UserModel getUserByTokenId(Long tokenId);
    UserModel getUser(Long userId);
    UserModel getUserByUsername(String username);
    UserModel register(UserModel user);
    UserModel updateUser(UserModel user);
}
