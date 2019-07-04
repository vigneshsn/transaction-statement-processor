package com.rabobank.statementprocessor.exceptions;

import com.rabobank.statementprocessor.models.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = {DocumentTypeNotSupportedException.class, CSVParseException.class,
            XMLParseException.class})
    public ResponseEntity<Error> handleBadUserInputException(Exception ex, WebRequest request) {
        Error error = new Error(ex.getMessage());
        log.error("handleBadUserInputException {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public ResponseEntity<Error> handleMaximumUploadSizeExceeded(Exception ex, WebRequest request) {
        Error error = new Error("Maximum file upload size exceeded - limit is 5MB");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Error> handleUnknownException(Exception ex, WebRequest request) {
        Error error = new Error("Server error - unable to process request temporarily");
        log.error("Global exception handler {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
