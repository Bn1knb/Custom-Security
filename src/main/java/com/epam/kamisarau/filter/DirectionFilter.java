package com.epam.kamisarau.filter;

import com.epam.kamisarau.utils.JdbcMysqlUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

@WebFilter(value = "/*", asyncSupported = true)
public class DirectionFilter implements Filter {
    private static final String TOKEN_COOKIE_NAME = "auth_token";
    private static final String AUTH_LOGIN_DIRECTION = "/auth";
    private static final String AUTH_APP_DIRECTION = "/application";
    private static final String EXPIRATION_DATE_COLUMN_LABEL = "expiration_date";
    private static final String RETRIEVE_TOKEN_SQL_STATEMENT = "SELECT expiration_date FROM token_storage WHERE token_value = ?";
    private Connection connection;
    private String tokenValue;

    public DirectionFilter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        try {
            this.connection = JdbcMysqlUtil.getMySQLConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request);
        Cookie[] cookies = request.getCookies();

        boolean isAppRequest = request.getRequestURI().startsWith(request.getContextPath() + AUTH_APP_DIRECTION);
        boolean isAuthRequest = request.getRequestURI().startsWith(request.getContextPath() + AUTH_LOGIN_DIRECTION);
        boolean isAuthenticated = cookies != null && hasTokenCookie(cookies) && checkToken(tokenValue);

        String direction = isAuthenticated ? AUTH_APP_DIRECTION + request.getRequestURI().substring(6) : AUTH_LOGIN_DIRECTION + request.getRequestURI().substring(6);
        String params = "";

        if (request.getQueryString() != null ) {
            params = new StringBuilder()
                    .append("?")
                    .append(request.getQueryString())
                    .toString();
        }

        if ( (isAppRequest && isAuthenticated) || isAuthRequest) {
            filterChain.doFilter(request, response);
        } else {
            RequestDispatcher dispatcher = wrapper.getRequestDispatcher(direction + params);
            dispatcher.forward(request, response);
        }
    }

    private boolean hasTokenCookie(Cookie[] cookies) {
        tokenValue = Arrays.stream(cookies)
                .filter(c -> c.getName().equalsIgnoreCase(TOKEN_COOKIE_NAME))
                    .map(Cookie::getValue)
                    .findAny()
                    .orElse(null);

        return tokenValue != null;
    }

    private boolean isTokenValid(Date tokenExpirationDate) {
        Date now = new Date();
        return (now.getTime() - tokenExpirationDate.getTime()) <= 0;
    }

    private boolean checkToken(String tokenValue) {
        try {
            PreparedStatement statement = connection.prepareStatement(RETRIEVE_TOKEN_SQL_STATEMENT);
            statement.setString(1, tokenValue);

            ResultSet expirationDate = statement.executeQuery();
            return expirationDate.next() && isTokenValid(expirationDate.getDate(EXPIRATION_DATE_COLUMN_LABEL));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void destroy() {
    }
}

