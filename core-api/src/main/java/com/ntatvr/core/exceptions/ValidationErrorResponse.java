package com.ntatvr.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ValidationErrorResponse {

  private String property;
  private String message;
}
