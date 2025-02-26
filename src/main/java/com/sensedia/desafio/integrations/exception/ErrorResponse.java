package com.sensedia.desafio.integrations.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private Date timestamp;
    private String errorCode;
    private String message;

}
