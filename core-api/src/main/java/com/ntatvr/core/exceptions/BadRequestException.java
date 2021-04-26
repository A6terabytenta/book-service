package com.ntatvr.core.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties({"stackTrace", "cause"})
public class BadRequestException extends RuntimeException {

  private static final long serialVersionUID = -4571886750682686253L;

  private final Date time;
  private final List<Object> errors;
  private final String errorCode;
  private final String internalMessage;
  private final String userMessage;

  public BadRequestException() {
    super(ErrorEnum.BAD_REQUEST_EXCEPTION.getMessage());
    this.errors = new ArrayList<>();
    this.time = new Date();
    this.errorCode = ErrorEnum.BAD_REQUEST_EXCEPTION.getCode();
    this.internalMessage = ErrorEnum.BAD_REQUEST_EXCEPTION.getMessage();
    this.userMessage = ErrorEnum.BAD_REQUEST_EXCEPTION.getUserMessage();
  }

  public BadRequestException(final Exception ex) {
    super(ErrorEnum.BAD_REQUEST_EXCEPTION.getMessage(), ex);
    this.errors = new ArrayList<>();
    this.time = new Date();
    this.errorCode = ErrorEnum.BAD_REQUEST_EXCEPTION.getCode();
    this.userMessage = ErrorEnum.BAD_REQUEST_EXCEPTION.getUserMessage();
    this.internalMessage = ExceptionUtils.getStackTrace(ex);
  }

  public void addError(final String propertyPath, final String message, final Object... messageArgs) {
    final ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
    validationErrorResponse.setProperty(propertyPath);
    validationErrorResponse.setMessage(String.format(message, messageArgs));
    this.errors.add(validationErrorResponse);
  }

  /**
   * Constructs the custom bad request error response with stack trace exception message.
   * <p>
   * The <code>internalMessage</code> will show all tracing errors using
   * {@link ExceptionUtils#getStackTrace(Throwable)}
   *
   * @param ex the original exception
   */
  public static BadRequestException withStackTrace(final Exception ex) {
    return new BadRequestException(ex);
  }

}
