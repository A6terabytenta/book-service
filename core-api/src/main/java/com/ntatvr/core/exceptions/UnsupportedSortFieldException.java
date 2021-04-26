package com.ntatvr.core.exceptions;

import org.springframework.http.HttpStatus;

@ErrorCode(status = HttpStatus.BAD_REQUEST, code = ErrorEnum.UNSUPPORTED_SORT_FIELD_NAME_EXCEPTION)
public class UnsupportedSortFieldException extends BookServiceRunTimeException {

  private static final long serialVersionUID = -8394205680350302323L;

  public UnsupportedSortFieldException() {
    super(ErrorEnum.UNSUPPORTED_SORT_FIELD_NAME_EXCEPTION);
  }
}
