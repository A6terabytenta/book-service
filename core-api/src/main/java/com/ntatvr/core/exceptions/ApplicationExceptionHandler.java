package com.ntatvr.core.exceptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {

  private static final String ERROR_CODE = "errorCode";

  private static final String EXCEPTION_LOG_TEXT = "[Book Service Exception]: ";

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(final MethodArgumentNotValidException ex) {
    log.error(EXCEPTION_LOG_TEXT, ex);
    final Map<String, String> errors = new HashMap<>();
    errors.put(ERROR_CODE, HttpStatus.BAD_REQUEST.toString());

    final ServiceErrorResponse errorResponse =
        new ServiceErrorResponse(
            new Date(),
            ErrorEnum.BAD_REQUEST_EXCEPTION.getCode(),
            ErrorEnum.BAD_REQUEST_EXCEPTION.getMessage(),
            ErrorEnum.BAD_REQUEST_EXCEPTION.getUserMessage(),
            new ArrayList<>());

    ex.getBindingResult()
        .getAllErrors()
        .forEach(error -> {
          final String fieldName = ((FieldError) error).getField();
          final String errorMessage = error.getDefaultMessage();
          errorResponse.addError(
              ValidationErrorResponse.builder()
                  .property(fieldName)
                  .message(errorMessage)
                  .build());
        });
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(BookServiceRunTimeException.class)
  public ResponseEntity<Object> handleBoServiceRunTimeException(final BookServiceRunTimeException e) {
    log.error(EXCEPTION_LOG_TEXT, e);
    final ErrorCode errorCode = e.getClass().getAnnotation(ErrorCode.class);
    if (errorCode != null) {
      final ServiceErrorResponse errorResponse = new ServiceErrorResponse(
          errorCode.code().getCode(),
          e.getMessage(),
          e.getUserMessage()
      );
      return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorCode.status());
    }

    return new ResponseEntity<>(new ServiceErrorResponse
        (e.getErrorCode(), e.getMessage(), e.getUserMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException e) {
    log.error(EXCEPTION_LOG_TEXT, e);
    return new ResponseEntity<>(new ServiceErrorResponse(
        e.getErrorCode(),
        e.getMessage(),
        e.getUserMessage()
    ), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ValueInstantiationException.class)
  public ResponseEntity<Object> handleValueInstantiationException(final ValueInstantiationException e) {
    log.error(EXCEPTION_LOG_TEXT, e);
    final ServiceErrorResponse errorResponse =
        new ServiceErrorResponse(HttpStatus.BAD_REQUEST.toString(), "JSON parse error", e.getMessage());
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleCustomBadRequestException(final BadRequestException ex) {
    log.error(EXCEPTION_LOG_TEXT, ex);
    return ResponseEntity.badRequest().body(ex);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex) {
    log.error(EXCEPTION_LOG_TEXT, ex);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(BadRequestException.withStackTrace(ex));
  }

  @ExceptionHandler({
      HttpMessageNotReadableException.class,
      MissingServletRequestParameterException.class
  })
  public ResponseEntity<Object> handleInvalidHttpRequest(final Exception ex) {
    log.error(EXCEPTION_LOG_TEXT, ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BadRequestException.withStackTrace(ex));
  }

  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<Object> handleJsonProcessingException(final JsonProcessingException ex) {
    log.error(EXCEPTION_LOG_TEXT, ex);
    final BadRequestException badRequestException = new BadRequestException();
    // Use original message from exception
    badRequestException.addError("JsonProcessingException", ex.getOriginalMessage());
    return ResponseEntity.badRequest().body(badRequestException);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex) {
    log.error(EXCEPTION_LOG_TEXT, ex);
    final ServiceErrorResponse errorResponse = new ServiceErrorResponse(
        new Date(),
        ErrorEnum.BAD_REQUEST_EXCEPTION.getCode(),
        ErrorEnum.BAD_REQUEST_EXCEPTION.getMessage(),
        ErrorEnum.BAD_REQUEST_EXCEPTION.getUserMessage(),
        new ArrayList<>());
    ex.getConstraintViolations().forEach(error -> errorResponse.addError(ValidationErrorResponse.builder()
        .property(error.getPropertyPath().toString())
        .message(error.getMessage())
        .build()));
    errorResponse.getErrors().sort(Comparator.comparing(ValidationErrorResponse::getProperty));
    return ResponseEntity.badRequest().body(errorResponse);
  }
}
