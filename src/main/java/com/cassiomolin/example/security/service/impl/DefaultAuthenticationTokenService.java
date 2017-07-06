package com.cassiomolin.example.security.service.impl;

import com.cassiomolin.example.security.api.AuthenticationTokenDetails;
import com.cassiomolin.example.security.domain.Authority;
import com.cassiomolin.example.security.exception.AuthenticationTokenRefreshmentException;
import com.cassiomolin.example.security.service.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Default implementation for the {@link AuthenticationTokenService}.
 *
 * @author cassiomolin
 */
@Service
public class DefaultAuthenticationTokenService implements AuthenticationTokenService {

    /**
     * How long the token is valid for (in seconds).
     */
    @Value("${authentication.jwt.validFor}")
    private Long validFor;

    /**
     * How many times the token can be refreshed.
     */
    @Value("${authentication.jwt.refreshLimit}")
    private Integer refreshLimit;

    @Autowired
    private JwtTokenIssuer tokenIssuer;

    @Autowired
    private JwtTokenParser tokenParser;

    @Override
    public String issueToken(String username, Set<Authority> authorities) {

        String id = generateTokenIdentifier();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenDetails authenticationTokenDetails = new AuthenticationTokenDetails.Builder()
                .withId(id)
                .withUsername(username)
                .withAuthorities(authorities)
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withRefreshCount(0)
                .withRefreshLimit(refreshLimit)
                .build();

        return tokenIssuer.issueToken(authenticationTokenDetails);
    }

    @Override
    public AuthenticationTokenDetails parseToken(String token) {
        return tokenParser.parseToken(token);
    }

    @Override
    public String refreshToken(AuthenticationTokenDetails currentTokenDetails) {

        if (!currentTokenDetails.isEligibleForRefreshment()) {
            throw new AuthenticationTokenRefreshmentException("This token cannot be refreshed.");
        }

        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenDetails newTokenDetails = new AuthenticationTokenDetails.Builder()
                .withId(currentTokenDetails.getId()) // Reuse the same id
                .withUsername(currentTokenDetails.getUsername())
                .withAuthorities(currentTokenDetails.getAuthorities())
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withRefreshCount(currentTokenDetails.getRefreshCount() + 1)
                .withRefreshLimit(refreshLimit)
                .build();

        return tokenIssuer.issueToken(newTokenDetails);
    }

    /**
     * Calculate the expiration date for a token.
     *
     * @param issuedDate
     * @return
     */
    private ZonedDateTime calculateExpirationDate(ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(validFor);
    }

    /**
     * Generate a token identifier.
     *
     * @return
     */
    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}
