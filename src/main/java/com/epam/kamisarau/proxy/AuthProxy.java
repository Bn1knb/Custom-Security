package com.epam.kamisarau.proxy;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.ProxyServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "authProxy",
        description = "redirects upcoming requests to custom auth app",
        urlPatterns = {"/auth/*"},
        asyncSupported = true
)
public class AuthProxy extends ProxyServlet {

    @Override
    protected String rewriteTarget(HttpServletRequest clientRequest) {
        String params = "";

        if (clientRequest.getQueryString() != null ) {
            params = "?" + clientRequest.getQueryString();
        }

        String requestedUri = clientRequest.getRequestURI().substring(7);
        return "http://localhost:9090/" + requestedUri + params;
    }


    @Override
    protected void onProxyResponseSuccess(HttpServletRequest clientRequest, HttpServletResponse proxyResponse, Response serverResponse) {
        super.onProxyResponseSuccess(clientRequest, proxyResponse, serverResponse);
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }


}
