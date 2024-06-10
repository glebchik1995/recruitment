package com.java.recruitment.web.controller;

import com.java.recruitment.repositoty.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(
            {
                    DataDeleteException.class,
                    DataSaveException.class,
                    DataUpdateException.class,
                    DataValidationException.class,
                    DataAlreadyExistException.class,
                    DataUploadException.class,
                    DataProcessingException.class
            }
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleCustomExceptions(Exception ex) {
        log.error("ResponseStatus: {}. Status code: {} {}", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAuthException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAuthException(DataAuthException ex) {
        log.error("ResponseStatus: FORBIDDEN. Status code: 403 {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(DataNotFoundException ex) {
        log.error("ResponseStatus: NOT_FOUND. Status code: 404 {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        log.error("ResponseStatus: INTERNAL_SERVER_ERROR. Status code: 500 {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
