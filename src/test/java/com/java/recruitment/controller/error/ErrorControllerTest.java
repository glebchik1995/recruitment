package com.java.recruitment.controller.error;

import com.java.recruitment.config.BaseIntegrationTest;
import com.java.recruitment.repositoty.exception.*;
import com.java.recruitment.web.controller.ControllerAdvice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class ErrorControllerTest extends BaseIntegrationTest {

    @Autowired
    private ControllerAdvice errorController;

    @Test
    void handleSaveExceptions_ReturnsBadRequest() {
        DataSaveException ex = new DataSaveException("Error saving data");
        ResponseEntity<String> response = errorController.handleCustomExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error saving data", response.getBody());
    }

    @Test
    void handleDeleteExceptions_ReturnsBadRequest() {
        DataSaveException ex = new DataSaveException("Error delete data");
        ResponseEntity<String> response = errorController.handleCustomExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error delete data", response.getBody());
    }

    @Test
    void handleUpdateExceptions_ReturnsBadRequest() {
        DataUpdateException ex = new DataUpdateException("Error update data");
        ResponseEntity<String> response = errorController.handleCustomExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error update data", response.getBody());
    }

    @Test
    void handleValidationExceptions_ReturnsBadRequest() {
        DataValidationException ex = new DataValidationException("Error validation data");
        ResponseEntity<String> response = errorController.handleCustomExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error validation data", response.getBody());
    }
    @Test
    void handleAlreadyExistExceptions_ReturnsBadRequest() {
        DataAlreadyExistException ex = new DataAlreadyExistException("Error of existing data");
        ResponseEntity<String> response = errorController.handleCustomExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error of existing data", response.getBody());
    }

    @Test
    void handleAuthException_ReturnsUnauthorized() {
        DataAuthException ex = new DataAuthException("Unauthorized access");
        ResponseEntity<String> response = errorController.handleAuthException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized access", response.getBody());
    }

    @Test
    void handleNotFoundException_ReturnsNotFound() {
        DataNotFoundException ex = new DataNotFoundException("Data not found");
        ResponseEntity<String> response = errorController.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Data not found", response.getBody());
    }

    @Test
    void handleOtherExceptions_ReturnsInternalServerError() {
        Exception ex = new Exception("Internal server error");
        ResponseEntity<String> response = errorController.handleOtherExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }
}
