package com.epam.kamisarau.auth.controller;

import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.TokenValueDto;
import com.epam.kamisarau.auth.model.dto.UserRegistrationDto;
import com.epam.kamisarau.auth.service.AuthService;
import com.epam.kamisarau.auth.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserAuthService userAuthService;
    private AuthService authService;

    @Autowired
    public RegistrationController(@Qualifier("hbUserService") UserAuthService userAuthService, @Qualifier("hbAuthService") AuthService authService) {
        this.userAuthService = userAuthService;
        this.authService = authService;
    }

    @GetMapping
    String getRegistrationPage() {
        return "register";
    }

    @PostMapping
    String doRegister(@ModelAttribute UserRegistrationDto userDto, HttpServletRequest request, HttpServletResponse response, Model model) {
        UserModel registeredUser = userAuthService.register(userDto);
        TokenModel newToken = authService.createToken();
        TokenValueDto tokenValue = authService.setTokenToUser(registeredUser, newToken);
        Cookie authCookie = new Cookie("auth_token", tokenValue.getTokenValue().toString());
        response.addCookie(authCookie);
        request.getSession().setAttribute("user", registeredUser);
       return "redirect:user"; //ny i kyda mi sobralis'??? ---> proxy?
    }
}
