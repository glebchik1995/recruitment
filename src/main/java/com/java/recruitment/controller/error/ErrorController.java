package com.java.recruitment.controller.error;

import com.java.recruitment.repositoty.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler({
            DataDeleteException.class,
            DataSaveException.class,
            DataUpdateException.class,
            DataValidationException.class,
            DataAlreadyExistException.class
    })
    public ResponseEntity<String> handleCustomExceptions(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex instanceof DataDeleteException || ex instanceof DataSaveException || ex instanceof DataUpdateException
                || ex instanceof DataValidationException || ex instanceof DataAlreadyExistException) {
            log.error("ResponseStatus: {}. Status code: {} {}", status, status.value(), ex.getMessage());
        }
        return new ResponseEntity<>(ex.getMessage(), status);
    }

    @ExceptionHandler(DataAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAuthException(DataAuthException ex) {
        log.error("ResponseStatus: UNAUTHORIZED. Status code: 401 {}", ex.getMessage());
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
