package com.cassiomolin.example.security.service;

import com.cassiomolin.example.security.api.AuthenticationTokenDetails;
import com.cassiomolin.example.security.domain.Authority;

import java.util.Set;

/**
 * Service which provides operations for authentication tokens.
 *
 * @author cassiomolin
 */
public interface AuthenticationTokenService {

    /**
     * Issue an authentication token for a user with the given authorities.
     *
     * @param username
     * @param authorities
     * @return
     */
    String issueToken(String username, Set<Authority> authorities);

    /**
     * Parse an authentication token.
     *
     * @param token
     * @return
     */
    AuthenticationTokenDetails parseToken(String token);

    /**
     * Refresh an authentication token.
     *
     * @param currentAuthenticationTokenDetails
     * @return
     */
    String refreshToken(AuthenticationTokenDetails currentAuthenticationTokenDetails);
}
