package com.ntatvr.core.exceptions;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

@Target({TYPE})
@Retention(RUNTIME)
public @interface ErrorCode {

    HttpStatus status();

    ErrorEnum code();
}
