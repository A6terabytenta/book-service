package com.ntatvr.core.exceptions;

import org.springframework.http.HttpStatus;

@ErrorCode(status = HttpStatus.BAD_REQUEST, code = ErrorEnum.UNSUPPORTED_FILTER_FIELD_NAME_EXCEPTION)
public class UnsupportedFilterFieldException extends BookServiceRunTimeException {

  private static final long serialVersionUID = -54810402693250835L;

  public UnsupportedFilterFieldException() {
    super(ErrorEnum.UNSUPPORTED_FILTER_FIELD_NAME_EXCEPTION);
  }
}
