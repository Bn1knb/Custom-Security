package com.epam.kamisarau.auth.controller;

import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.UserRegistrationDto;
import com.epam.kamisarau.auth.service.AuthService;
import com.epam.kamisarau.auth.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserAuthService userAuthService;
    private AuthService authService;

    @Autowired
    public RegistrationController(@Qualifier("hbUserService") UserAuthService userAuthService,
                                  @Qualifier("hbAuthService") AuthService authService) {
        this.userAuthService = userAuthService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity createUser(@Valid @RequestBody UserRegistrationDto registrationForm,
                                     HttpServletResponse response) {

        UserModel registeredUser = registrationForm.getUserModelFromRegistrationDto();

        TokenModel newToken = authService.setTokenToUser(registeredUser);
        registeredUser = userAuthService.register(registeredUser);
        Cookie authCookie = new Cookie("auth_token", newToken.getTokenValue());
        authCookie.setPath("/");
        response.addCookie(authCookie);

        URI location = ServletUriComponentsBuilder
                .fromHttpUrl("http://localhost:80")
                .path("/proxy/users/{userId}")
                .buildAndExpand(registeredUser.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }
}
