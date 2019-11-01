package com.epam.kamisarau.auth.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    private static final String LOGIN_PAGE_URI = "/login";
    private static final String REGISTRATION_PAGE_URI = "/register";
    private static final String SWAGGER_PATTERN = "/swagger";

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI().substring(5);

        boolean isLoginRequest = (request.getContextPath() + LOGIN_PAGE_URI).equals(request.getRequestURI());
        boolean isRegisterRequest = (request.getContextPath() + REGISTRATION_PAGE_URI).equals(request.getRequestURI());
        boolean isSwaggerRequest = uri.startsWith(SWAGGER_PATTERN) || uri.startsWith("/v2/api-docs") || uri.startsWith("/webjars/springfox");

        if (isRegisterRequest || isLoginRequest || isSwaggerRequest) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect("http://localhost:80/proxy/auth" + LOGIN_PAGE_URI);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}
