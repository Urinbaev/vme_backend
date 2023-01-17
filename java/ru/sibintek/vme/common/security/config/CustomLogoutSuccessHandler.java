package ru.sibintek.vme.common.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final static String REDIRECT_URI_PARAMETER_NAME = "spa_uri";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException{

        String redirectUri = request.getParameter(REDIRECT_URI_PARAMETER_NAME);

        response.sendRedirect(Objects.requireNonNullElse(redirectUri, "/"));

    }

}
