package com.epam.kamisarau.auth.service;

import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.UserCredsDto;
import com.epam.kamisarau.auth.model.dto.UserRegistrationDto;

public interface UserAuthService {
    UserModel register(UserModel userModel);
    UserModel login(UserCredsDto userCredsDto);
}
