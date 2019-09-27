package com.epam.kamisarau.auth.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
@Order(2)
public class LoginFilter implements Filter {
    private static final String LOGIN_PAGE_URI = "/login";
    private static final String REGISTRATION_PAGE_URI = "/register";
    private static final String USER_PAGE_URI = "/user";
    private static final String USER_ATTRIBUTE_NAME = "user";

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        boolean isLoginRequest = (request.getContextPath() + LOGIN_PAGE_URI).equals(request.getRequestURI());
        boolean isRegisterRequest = (request.getContextPath() + REGISTRATION_PAGE_URI).equals(request.getRequestURI());
        boolean loggedIn = request.getSession() != null && request.getSession().getAttribute(USER_ATTRIBUTE_NAME) != null;

        if (loggedIn) {

            if (isRegisterRequest || isLoginRequest) {
                response.sendRedirect(request.getContextPath() + USER_PAGE_URI);
            } else {
                filterChain.doFilter(request, response);
            }

        } else {
            if (isRegisterRequest || isLoginRequest) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + LOGIN_PAGE_URI);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
