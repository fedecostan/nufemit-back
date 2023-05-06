package com.nufemit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handleEntityNotFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({DuplicateInformationException.class})
    public ResponseEntity<String> handleDuplicateInformationException() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UniqueRatingException.class})
    public ResponseEntity<String> handleUniqueRatingException() {
        return new ResponseEntity<>("You can only review the same user once", HttpStatus.PRECONDITION_FAILED);
    }
}
