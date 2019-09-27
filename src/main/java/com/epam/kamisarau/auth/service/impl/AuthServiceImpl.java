package com.epam.kamisarau.auth.service.impl;

import com.epam.kamisarau.auth.exception.NoTokenFound;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.TokenValueDto;
import com.epam.kamisarau.auth.repository.AuthRepository;
import com.epam.kamisarau.auth.repository.TokenRepository;
import com.epam.kamisarau.auth.repository.UserRepository;
import com.epam.kamisarau.auth.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
    private TokenRepository tokenRepository;
    private AuthRepository authRepository;
    private UserRepository userRepository;

    public AuthServiceImpl(TokenRepository tokenRepository, AuthRepository authRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TokenValueDto setTokenToUser(UserModel user, TokenModel token) {
        String username = user.getUsername();
        Long tokenId = token.getId();
        authRepository.setTokenToUser(username, tokenId);
        return new TokenValueDto().getTokenValueFromToken(token);
    }

    @Override
    public void deleteTokenOfUser(UserModel user) {
        String username = user.getUsername();
        Long tokenId = authRepository.getTokenIdByUser(username);
        authRepository.deleteTokenOfUser(username);
        tokenRepository.deleteTokenById(tokenId);
    }

    @Override
    public UserModel loginByToken(TokenModel token) {
        Long tokenId = token.getId();
        String username = authRepository.getUserByTokenId(tokenId);
        return userRepository.getUserByUsername(username);
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
        return tokenRepository.updateToken(tokenModel.getId(), createToken());
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
        String username = user.getUsername();
        Long tokenId = authRepository.getTokenIdByUser(username);
        return tokenRepository.getToken(tokenId);
    }
}
