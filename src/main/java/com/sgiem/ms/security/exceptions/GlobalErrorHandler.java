package com.sgiem.ms.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception exception, WebRequest req){
        ErrorResponse res = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), req.getDescription(false));

        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRunTimeException(RuntimeException exception, WebRequest req){
        ErrorResponse res = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), req.getDescription(false)); //si pongo true tiene mas detalle

        return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
    }
}
