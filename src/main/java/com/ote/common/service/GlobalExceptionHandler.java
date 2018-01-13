package com.ote.common.service;

import com.ote.common.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handle(Throwable exception) {
        log.error(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handle(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        Error error = new Error();
        exception.getBindingResult().getFieldErrors().stream().
                map(p -> p.getObjectName() + ": " + p.getField() + " " + p.getDefaultMessage()).
                forEach(m -> error.getMessages().add(m));
        return error;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Error handle(AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }
}
