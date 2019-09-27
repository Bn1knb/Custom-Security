package com.epam.kamisarau.auth.service.impl.hibernate;

import com.epam.kamisarau.auth.exception.NoTokenFound;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.TokenValueDto;
import com.epam.kamisarau.auth.repository.TokenRepository;
import com.epam.kamisarau.auth.repository.UserRepository;
import com.epam.kamisarau.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service("hbAuthService")
@Qualifier("hbAuthService")
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
    public boolean isTokenPresent(Long tokenValue) {
        try {
            tokenRepository.getTokenByValue(tokenValue);
        } catch (NoTokenFound e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid(TokenModel tokenModel) {
        Long tokenValue = tokenModel.getTokenValue();
        return isTokenNonExpired(tokenModel) && isTokenPresent(tokenValue);
    }

    @Override
    public TokenModel updateToken(TokenModel tokenModel) {
        return null;
    }

    @Override
    public TokenModel findTokenByTokenValue(Long tokenValue) {
        return tokenRepository.getTokenByValue(tokenValue);
    }

    @Override
    public TokenModel createToken() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, 1);
        Date expirationDate = calendar.getTime();
        Long tokenValue = new Random().nextLong();
        return tokenRepository.store(
                new TokenModel()
                        .setExpirationDate(expirationDate)
                        .setId(null)
                        .setTokenValue(tokenValue)
        );
    }

    @Override
    public TokenModel getTokenOfUser(UserModel user) {
        return user.getToken();
    }

    @Override
    public TokenValueDto setTokenToUser(UserModel user, TokenModel token) {
        user.setToken(token);
        return new TokenValueDto().getTokenValueFromToken(token);
    }

    @Override
    public void deleteTokenOfUser(UserModel user) {
        tokenRepository.deleteTokenById(user.getToken().getId());
        user.setToken(null);
    }
}
