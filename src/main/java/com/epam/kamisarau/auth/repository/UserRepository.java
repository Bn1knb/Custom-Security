package com.epam.kamisarau.auth.repository;

import com.epam.kamisarau.auth.model.UserModel;

public interface UserRepository {
    /** deprecated */
    @Deprecated
    UserModel getUser(Long userId);
    UserModel getUserByUsername(String username);
    UserModel doRegister(UserModel user);
}
