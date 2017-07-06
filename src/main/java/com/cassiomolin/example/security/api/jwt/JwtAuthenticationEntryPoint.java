package com.cassiomolin.example.security.api.jwt;

import com.cassiomolin.example.common.api.model.ApiErrorDetails;
import com.cassiomolin.example.security.exception.InvalidAuthenticationTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Entry point for JWT token-based authentication. Simply returns error details related to authentication failures.
 *
 * @author cassiomolin
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        HttpStatus status;
        ApiErrorDetails errorDetails = new ApiErrorDetails();

        if (authException instanceof InvalidAuthenticationTokenException) {
            status = HttpStatus.UNAUTHORIZED;
            errorDetails.setTitle(authException.getMessage());
            errorDetails.setMessage(authException.getCause().getMessage());
        } else {
            status = HttpStatus.FORBIDDEN;
            errorDetails.setTitle(status.getReasonPhrase());
            errorDetails.setMessage(authException.getMessage());
        }

        errorDetails.setStatus(status.value());
        errorDetails.setPath(request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), errorDetails);
    }
}