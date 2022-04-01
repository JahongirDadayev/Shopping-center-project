package com.example.restful.exception.validation;

import com.example.restful.exception.ExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> validationHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();
        ExceptionModel exceptionModel = new ExceptionModel(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionModel, HttpStatus.BAD_REQUEST);
    }
}
