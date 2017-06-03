package com.cassiomolin.example.api.security.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for JWT token-based authentication.
 * <p>
 * Authentication tokens must be sent in the <code>Authorization</code> header and prefixed with <code>Bearer</code>:
 * <p>
 * <pre>
 *     Authorization: Bearer xxx.yyy.zzz
 * </pre>
 *
 * @author cassiomolin
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager,
                                        AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {

                String authenticationToken = authorizationHeader.substring(7);
                Authentication authenticationRequest = new JwtAuthenticationToken(authenticationToken);
                Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authenticationResult);
                SecurityContextHolder.setContext(context);

            } catch (AuthenticationException e) {
                SecurityContextHolder.clearContext();
                authenticationEntryPoint.commence(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}