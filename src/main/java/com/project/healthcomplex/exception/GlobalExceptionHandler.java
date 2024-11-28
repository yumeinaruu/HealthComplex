package com.project.healthcomplex.exception;

import com.project.healthcomplex.exception.custom.*;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({CustomValidationException.class, ValidationException.class})
    public ResponseEntity<HttpStatus> handleCustomValidationException(CustomValidationException ex) {
        log.error("Error occurred: " + ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<HttpStatus> handleNoSuchUserException(NoSuchUserException ex) {
        log.error("Error occurred: " + ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchDataInDbException.class)
    public ResponseEntity<HttpStatus> handleNoSuchDataInDbException(NoSuchDataInDbException ex) {
        log.error("Error occurred: " + ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotThatUserUpdatesRecord.class)
    public ResponseEntity<HttpStatus> handleNotThatUserUpdatesRecord(NotThatUserUpdatesRecord ex) {
        log.error("Forbidden: " + ex);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SameUserInDatabase.class)
    public ResponseEntity<HttpStatus> handleSameUserInDatabaseException(SameUserInDatabase ex) {
        log.error("Error occurred: " + ex);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<HttpStatus> handleSQLException(SQLException ex) {
        log.error("Error occurred: " + ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpStatus> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Error occurred: " + ex);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<HttpStatus> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.warn("Forbidden: " + ex);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<HttpStatus> usernameNotFound(Exception exception) {
        log.error(exception + "");
        return new ResponseEntity<>(HttpStatus.valueOf(401));
    }
}
