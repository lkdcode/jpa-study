package com.study.jpa.chap05_practice.exception;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String test() {
        return "testttttt message";
    }
}
