package com.ntatvr.core.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceErrorResponse {

  private Date time;

  private String errorCode;

  private String internalMessage;

  private String userMessage;

  private List<ValidationErrorResponse> errors;

  public ServiceErrorResponse(final String errorCode, final String internalMessage, final String userMessage) {
    this.time = new Date();
    this.errorCode = errorCode;
    this.internalMessage = internalMessage;
    this.userMessage = userMessage;
  }

  public void addError(final ValidationErrorResponse error) {
    Optional.ofNullable(errors).orElse(new ArrayList<>()).add(error);
  }
}
