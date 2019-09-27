package com.epam.kamisarau.auth.controller;

import com.epam.kamisarau.auth.exception.InvalidUserNameOrPassword;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.TokenValueDto;
import com.epam.kamisarau.auth.model.dto.UserCredsDto;
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
@RequestMapping("/login")
public class LoginController {
    private UserAuthService userAuthService;
    private AuthService authService;
    private static final String USER_ATTRIBUTE_NAME = "user";

    @Autowired
    public LoginController(@Qualifier("hbUserService") UserAuthService userAuthService, @Qualifier("hbAuthService") AuthService authService) {
        this.userAuthService = userAuthService;
        this.authService = authService;
    }

    @GetMapping
    String getLoginPage() {
        return "login";
    }

    @PostMapping
    String doLogin(@ModelAttribute UserCredsDto credentials,
                  HttpServletRequest request,
                  HttpServletResponse response, Model model) {

        //TODO find user check token is token is valid then add token to coockie and user to session
        //TODO else generate new token then update previous token
        //TODO think of cheking token validity from all the db every hour
        //TODO if all done then send request from proxy to the main app wich redirects it

        try {
            UserModel logginedUser = userAuthService.login(credentials);
            authService.deleteTokenOfUser(logginedUser);
            TokenModel newToken = authService.createToken();
            TokenValueDto tokenValue = authService.setTokenToUser(logginedUser, newToken);
            Cookie authCookie = new Cookie("auth_token", tokenValue.getTokenValue().toString());
            response.addCookie(authCookie);
            request.getSession().setAttribute(USER_ATTRIBUTE_NAME, logginedUser);
        } catch (InvalidUserNameOrPassword e) {
            model.addAttribute("error" , "bad credentials");
            getLoginPage();
        }

        return "redirect:user";
    }
}
