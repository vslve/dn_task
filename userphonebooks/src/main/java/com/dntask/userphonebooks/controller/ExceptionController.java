package com.dntask.userphonebooks.controller;

import com.dntask.userphonebooks.exception.RecordNotFoundException;
import com.dntask.userphonebooks.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ControllerUtils.ExceptionMessage("Incorrect path variable type"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserException(UserNotFoundException e) {
        return new ResponseEntity<>(new ControllerUtils.ExceptionMessage(e.getMessage()), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<?> handleRecordException(RecordNotFoundException e) {
        return new ResponseEntity<>(new ControllerUtils.ExceptionMessage(e.getMessage()), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new ControllerUtils.ExceptionMessage("Incorrect request body"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(new ControllerUtils.ExceptionMessage("Incorrect path variable"));
    }
}
