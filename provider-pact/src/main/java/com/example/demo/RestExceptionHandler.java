package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {NotificationNotFoundException.class})
    public ResponseEntity signupFailed(Exception ex, WebRequest req) {
        Map<String, String> errorMsg = new HashMap<>();
        errorMsg.put("code", "not_found");
        errorMsg.put("message", ex.getMessage());
        return status(HttpStatus.NOT_FOUND).body(errorMsg);
    }
}
