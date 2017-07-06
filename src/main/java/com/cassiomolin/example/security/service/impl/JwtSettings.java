package com.cassiomolin.example.security.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Settings for signing and verifying JWT tokens.
 *
 * @author cassiomolin
 */
@Component
class JwtSettings {

    /**
     * Secret for signing and verifying the token signature.
     */
    @Value("${authentication.jwt.secret}")
    private String secret;

    /**
     * Allowed clock skew for verifying the token signature (in seconds).
     */
    @Value("${authentication.jwt.clockSkew}")
    private Long clockSkew;

    /**
     * Identifies the recipients that the JWT token is intended for.
     */
    @Value("${authentication.jwt.audience}")
    private String audience;

    /**
     * Identifies the JWT token issuer.
     */
    @Value("${authentication.jwt.issuer}")
    private String issuer;

    /**
     * JWT claim for the authorities.
     */
    @Value("${authentication.jwt.claimNames.authorities}")
    private String authoritiesClaimName;

    /**
     * JWT claim for the token refreshment count.
     */
    @Value("${authentication.jwt.claimNames.refreshCount}")
    private String refreshCountClaimName;

    /**
     * JWT claim for the maximum times that a token can be refreshed.
     */
    @Value("${authentication.jwt.claimNames.refreshLimit}")
    private String refreshLimitClaimName;

    public String getSecret() {
        return secret;
    }

    public Long getClockSkew() {
        return clockSkew;
    }

    public String getAudience() {
        return audience;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAuthoritiesClaimName() {
        return authoritiesClaimName;
    }

    public String getRefreshCountClaimName() {
        return refreshCountClaimName;
    }

    public String getRefreshLimitClaimName() {
        return refreshLimitClaimName;
    }
}
