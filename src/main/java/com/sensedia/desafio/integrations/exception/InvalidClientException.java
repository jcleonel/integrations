package com.sensedia.desafio.integrations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidClientException extends AuthenticationException {

    public InvalidClientException(String message) {
        super(message);
    }

}
