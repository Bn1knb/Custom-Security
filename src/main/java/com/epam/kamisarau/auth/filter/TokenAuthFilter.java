package com.epam.kamisarau.auth.filter;

import com.epam.kamisarau.auth.exception.NoTokenFound;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.service.AuthService;
import com.epam.kamisarau.auth.service.impl.jdbc.AuthServiceImpl;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
@Order(2)
public class TokenAuthFilter implements Filter {
    private TokenModel token;
    private AuthService authService;
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String TOKEN_COOKIE_NAME = "auth_token";

    public TokenAuthFilter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        if(authService == null){
            ServletContext servletContext = filterConfig.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            assert webApplicationContext != null;
            authService = webApplicationContext.getBean(AuthServiceImpl.class);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Cookie[] cookies = request.getCookies();

        if ((cookies != null && hasTokenCookie(cookies)) && authService.isValid(token)) {
            UserModel associatedUser = authService.loginByToken(token);
            request.getSession().setAttribute(USER_ATTRIBUTE_NAME, associatedUser);
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean hasTokenCookie(Cookie[] cookies) {
        for (Cookie c : cookies) {
            if (c.getName().equalsIgnoreCase(TOKEN_COOKIE_NAME)) {
                Long tokenValue = Long.valueOf(c.getValue());
                try {
                    token = authService.findTokenByTokenValue(tokenValue);
                } catch (NoTokenFound e) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }
}

