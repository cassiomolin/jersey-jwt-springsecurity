package com.cassiomolin.example.api.security.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown if an authentication token is invalid.
 *
 * @author cassiomolin
 */
public class InvalidAuthenticationTokenException extends AuthenticationException {

    public InvalidAuthenticationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
