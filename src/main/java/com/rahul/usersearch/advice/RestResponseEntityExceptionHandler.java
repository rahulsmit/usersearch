package com.rahul.usersearch.advice;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.OpenSearchStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value
      = { IOException.class, IllegalStateException.class, java.net.ConnectException.class })
  protected ResponseEntity<Object> handlIOError(Exception ex, WebRequest request) {
    log.error("Error handled in advice: ", ex);
    return handleExceptionInternal(ex, "INTERNAL SERVER ERROR",
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @ExceptionHandler(value
      = { OpenSearchStatusException.class })
  protected ResponseEntity<Object> handleIndexException(Exception ex, WebRequest request) {
    log.error("Error handled in advice: ", ex);
    return handleExceptionInternal(ex, "PLEASE CREATE INDEX FIRST",
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}