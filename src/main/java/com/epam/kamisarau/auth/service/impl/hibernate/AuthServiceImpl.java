package com.epam.kamisarau.auth.service.impl.hibernate;

import com.epam.kamisarau.auth.exception.NoTokenFound;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.repository.TokenRepository;
import com.epam.kamisarau.auth.repository.UserRepository;
import com.epam.kamisarau.auth.service.AuthService;
import com.epam.kamisarau.auth.utils.TokenValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service("hbAuthService")
@Qualifier("hbAuthService")
@Transactional
public class AuthServiceImpl implements AuthService {
    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(@Qualifier("hbTokenRepositoryImpl") TokenRepository tokenRepository, @Qualifier("hbUserRepositoryImpl") UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserModel loginByToken(TokenModel token) {
        return userRepository.getUserByTokenId(token.getId());
    }

    @Override
    public boolean isTokenNonExpired(TokenModel token) {
        Date now = new Date();
        Date tokenExpirationDate = token.getExpirationDate();
        return (now.getTime() - tokenExpirationDate.getTime()) <= 0;
    }

    @Override
    public boolean isTokenPresent(String tokenValue) {
        try {
            tokenRepository.getTokenByValue(tokenValue);
        } catch (NoTokenFound e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid(TokenModel tokenModel) {
        String tokenValue = tokenModel.getTokenValue();
        return isTokenNonExpired(tokenModel) && isTokenPresent(tokenValue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TokenModel updateToken(TokenModel tokenModel) {
        UserModel user = userRepository.getUserByTokenId(tokenModel.getId());
        deleteTokenOfUser(user);
        TokenModel newToken = setTokenToUser(user);
        userRepository.updateUser(user);
        return newToken;
    }

    @Override
    public TokenModel findTokenByTokenValue(String tokenValue) {
        return tokenRepository.getTokenByValue(tokenValue);
    }

    @Override
    public TokenModel createToken(String username) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, 1);
        Date expirationDate = calendar.getTime();
        String tokenValue = TokenValueUtils.encrypt(username);

        return tokenRepository.store(
                new TokenModel()
                        .setExpirationDate(expirationDate)
                        .setTokenValue(tokenValue)
        );
    }

    @Override
    public TokenModel getTokenOfUser(UserModel user) {
        return user.getToken();
    }

    @Override
    public TokenModel setTokenToUser(UserModel user) {
        TokenModel token = createToken(user.getUsername());
        user.setToken(token);
        return token;
    }

    @Override
    public void deleteTokenOfUser(UserModel user) {
        tokenRepository.deleteTokenOfUser(user);
    }
}
