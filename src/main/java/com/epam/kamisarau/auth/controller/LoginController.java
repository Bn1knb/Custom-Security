package com.epam.kamisarau.auth.controller;

import com.epam.kamisarau.auth.exception.InvalidUserNameOrPassword;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.model.dto.UserCredsDto;
import com.epam.kamisarau.auth.service.AuthService;
import com.epam.kamisarau.auth.service.UserAuthService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Api(value = "User auth system")
public class LoginController {
    private UserAuthService userAuthService;
    private AuthService authService;

    private static final String TOKEN_COOKIE_NAME = "auth_token";
    private static final String USER_ATTRIBUTE_NAME = "user";

    @Autowired
    public LoginController(@Qualifier("hbUserService") UserAuthService userAuthService,
                           @Qualifier("hbAuthService") AuthService authService) {
        this.userAuthService = userAuthService;
        this.authService = authService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "login user by his credentials", response = UserModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully loggined"),
            @ApiResponse(code = 400, message = "bad credentials")
    })
    public ResponseEntity login(@ApiParam(value = "User's username and password", required = true)
                                @Valid @RequestBody UserCredsDto credentials,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        try {

            UserModel logginedUser = userAuthService.login(credentials);

            TokenModel newToken = authService.updateToken(logginedUser.getToken());
            Cookie authCookie = new Cookie(TOKEN_COOKIE_NAME, newToken.getTokenValue());
            authCookie.setPath("/");
            response.addCookie(authCookie);
            request.getSession().setAttribute(USER_ATTRIBUTE_NAME, logginedUser);

            return ResponseEntity.ok(logginedUser);

        } catch (InvalidUserNameOrPassword e) {
            throw new InvalidUserNameOrPassword();
        }
    }

    @GetMapping
    //todo check why spring ignoring viewcontroller
    public String returnLoginPage() {
        return "login";
    }
}
