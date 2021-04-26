package com.ntatvr.core.exceptions;

import lombok.Getter;

@Getter
public enum ErrorEnum {

  BAD_REQUEST_EXCEPTION("0000001", "Bad Request", "Unable to process the request."),
  ENTITY_NOT_FOUND_EXCEPTION("0000002", "Entity not found", "Could not find entity that you are looking for"),
  UNSUPPORTED_SORT_FIELD_NAME_EXCEPTION("0000003", "The value of Sort request parameter is unsupported.",
  "The value of Sort request parameter is unsupported."),
  UNSUPPORTED_FILTER_FIELD_NAME_EXCEPTION("0000004","The fields request parameter are unsupported.",
  "The fields request parameter are unsupported.");

  private String code;
  private String message;
  private String userMessage;

  ErrorEnum(final String code, final String message, final String userMessage) {
    this.code = code;
    this.message = message;
    this.userMessage = userMessage;
  }
}
