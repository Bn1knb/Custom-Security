package com.epam.kamisarau.auth.service.impl.hibernate;

import com.epam.kamisarau.auth.exception.InvalidUserNameOrPassword;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.UserCredsDto;
import com.epam.kamisarau.auth.model.dto.UserRegistrationDto;
import com.epam.kamisarau.auth.repository.UserRepository;
import com.epam.kamisarau.auth.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("hbUserService")
@Qualifier("hbUserService")
public class UserServiceImpl implements UserAuthService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("hbUserRepositoryImpl") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserModel register(UserRegistrationDto userRegistrationDto) {
        return userRepository.register(userRegistrationDto.getUserModelFromRegistrationDto());
    }

    @Override
    public UserModel login(UserCredsDto userCredentials) {
        UserModel user = userRepository.getUserByUsername(userCredentials.getUsername());

        if (user.getUsername().equals(userCredentials.getPassword())) {
            return user;
        } else {
            throw new InvalidUserNameOrPassword();
        }
    }
}
