package com.ntatvr.core.exceptions;

import org.springframework.http.HttpStatus;

@ErrorCode(status = HttpStatus.BAD_REQUEST, code = ErrorEnum.ENTITY_NOT_FOUND_EXCEPTION)
public class EntityNotFoundException extends BookServiceRunTimeException {

  private static final long serialVersionUID = -2328118315663611393L;

  public EntityNotFoundException() {
    super(ErrorEnum.ENTITY_NOT_FOUND_EXCEPTION);
  }
}
