package com.java.recruitment.controller.error;

import com.java.recruitment.BaseIntegrationTest;
import com.java.recruitment.repositoty.exception.*;
import com.java.recruitment.web.controller.ControllerAdvice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerAdviceTest extends BaseIntegrationTest {
    @Autowired
    private ControllerAdvice controllerAdvice;

    @Test
    public void testHandleCustomExceptions() {
        DataSaveException ex = new DataSaveException("Error saving data");
        ResponseEntity<String> response = controllerAdvice.handleCustomExceptions(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error saving data", response.getBody());
    }

    @Test
    void handleUpdateExceptions_ReturnsBadRequest() {
        DataUpdateException ex = new DataUpdateException("Error update data");
        ResponseEntity<String> response = controllerAdvice.handleCustomExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error update data", response.getBody());
    }

    @Test
    void handleValidationExceptions_ReturnsBadRequest() {
        DataValidationException ex = new DataValidationException("Error validation data");
        ResponseEntity<String> response = controllerAdvice.handleCustomExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error validation data", response.getBody());
    }
    @Test
    void handleAlreadyExistExceptions_ReturnsBadRequest() {
        DataAlreadyExistException ex = new DataAlreadyExistException("Error of existing data");
        ResponseEntity<String> response = controllerAdvice.handleCustomExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error of existing data", response.getBody());
    }


    @Test
    public void testHandleAuthException() {
        DataAuthException ex = new DataAuthException("Authentication error");
        ResponseEntity<String> response = controllerAdvice.handleAuthException(ex);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Authentication error", response.getBody());
    }

    @Test
    public void testHandleNotFoundException() {
        DataNotFoundException ex = new DataNotFoundException("Data not found");
        ResponseEntity<String> response = controllerAdvice.handleNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Data not found", response.getBody());
    }

    @Test
    public void testHandleOtherExceptions() {
        Exception ex = new Exception("Internal server error");
        ResponseEntity<String> response = controllerAdvice.handleOtherExceptions(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }
}
