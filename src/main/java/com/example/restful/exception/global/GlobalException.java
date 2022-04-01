package com.example.restful.exception.global;

import com.example.restful.exception.ExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> globalException(RuntimeException e){
        ExceptionModel exceptionModel = new ExceptionModel(e.getMessage(), LocalDateTime.now());
        e.printStackTrace();
        return new ResponseEntity<>(exceptionModel, HttpStatus.BAD_REQUEST);

    }
}
