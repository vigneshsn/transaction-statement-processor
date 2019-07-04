package com.vigneshsn.statementprocessor.exceptions;

import com.vigneshsn.statementprocessor.models.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = {DocumentTypeNotSupportedException.class})
    public ResponseEntity<Error> handleUnSupportedDocumentTypeException(Exception ex, WebRequest request) {
        Error error = new Error(ex.getMessage());
        log.error("handleUnSupportedDocumentTypeException {}", ex.getMessage());
        return ResponseEntity
                .badRequest().body(error);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Error> handleUnknownException(Exception ex, WebRequest request) {
        Error error = new Error("Server error - unable to process request temporarily");
        log.error("Global exception handler {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
