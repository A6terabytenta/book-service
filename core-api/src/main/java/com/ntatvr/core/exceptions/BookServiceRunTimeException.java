package com.ntatvr.core.exceptions;

import lombok.Getter;

/*
 * Super Class for Book Service Runtime Exceptions
 * All Custom Service Exceptions are expected to extend this Class
 * */
@Getter
public class BookServiceRunTimeException extends RuntimeException {

  private static final long serialVersionUID = -3744625808620854176L;

  private final String errorCode;

  private final String userMessage;

  protected BookServiceRunTimeException(final ErrorEnum errorEnum) {
    super(errorEnum.getMessage());
    this.errorCode = errorEnum.getCode();
    this.userMessage = errorEnum.getUserMessage();
  }
}
