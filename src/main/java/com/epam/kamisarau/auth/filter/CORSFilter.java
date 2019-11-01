package com.epam.kamisarau.auth.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(asyncSupported = true, urlPatterns = { "/*" })
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "http://localhost");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET, POST");

        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        chain.doFilter(request, resp);
    }

    @Override
    public void destroy() {
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}