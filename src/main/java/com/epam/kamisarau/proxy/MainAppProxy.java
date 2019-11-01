package com.epam.kamisarau.proxy;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.ProxyServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "applicationProxy",
        description = "redirects upcoming requests to custom app",
        urlPatterns = {"/application/*"},
        asyncSupported = true
)
public class MainAppProxy extends ProxyServlet {

    @Override
    protected String rewriteTarget(HttpServletRequest clientRequest) {
        String params = "";

        if (clientRequest.getQueryString() != null ) {
            params = "?" + clientRequest.getQueryString();
        }

        String requestedUri = clientRequest.getRequestURI().substring(18);
        return "http://localhost:8080/" + requestedUri + params;
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
