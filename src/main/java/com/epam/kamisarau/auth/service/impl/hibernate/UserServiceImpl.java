package com.epam.kamisarau.auth.service.impl.hibernate;

import com.epam.kamisarau.auth.exception.InvalidUserNameOrPassword;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.UserCredsDto;
import com.epam.kamisarau.auth.repository.UserRepository;
import com.epam.kamisarau.auth.service.UserAuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("hbUserService")
@Qualifier("hbUserService")
@Transactional
public class UserServiceImpl implements UserAuthService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("hbUserRepositoryImpl") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserModel register(UserModel userModel) {
        userModel.setPassword(BCrypt.hashpw(userModel.getPassword(), BCrypt.gensalt()));
        return userRepository.register(userModel);
    }

    @Override
    public UserModel login(UserCredsDto userCredentials) {
        UserModel user = userRepository.getUserByUsername(userCredentials.getUsername());

        if (BCrypt.checkpw(userCredentials.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new InvalidUserNameOrPassword();
        }
    }
}
