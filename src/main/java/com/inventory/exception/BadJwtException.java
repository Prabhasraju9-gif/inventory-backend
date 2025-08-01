package com.inventory.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // Use UNAUTHORIZED for JWT issues
public class BadJwtException extends AuthenticationException {
    public BadJwtException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadJwtException(String msg) {
        super(msg);
    }
}
