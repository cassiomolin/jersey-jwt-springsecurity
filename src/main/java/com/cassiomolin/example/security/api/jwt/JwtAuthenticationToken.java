package com.cassiomolin.example.security.api.jwt;

import com.cassiomolin.example.security.api.AuthenticationTokenDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * An {@link org.springframework.security.core.Authentication} implementation that is designed for presentation of a JWT token.
 *
 * @author cassiomolin
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String authenticationToken;
    private UserDetails userDetails;
    private AuthenticationTokenDetails authenticationTokenDetails;

    /**
     * Creates a {@link JwtAuthenticationToken} instance for an unauthenticated token.
     *
     * @param authenticationToken
     */
    public JwtAuthenticationToken(String authenticationToken) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.authenticationToken = authenticationToken;
        this.setAuthenticated(false);
    }

    /**
     * Creates a {@link JwtAuthenticationToken} instance for an authenticated token.
     *
     * @param userDetails
     * @param authenticationTokenDetails
     * @param authorities
     */
    public JwtAuthenticationToken(UserDetails userDetails, AuthenticationTokenDetails authenticationTokenDetails,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userDetails = userDetails;
        this.authenticationTokenDetails = authenticationTokenDetails;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted. Use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return authenticationToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public Object getDetails() {
        return authenticationTokenDetails;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.authenticationToken = null;
    }
}