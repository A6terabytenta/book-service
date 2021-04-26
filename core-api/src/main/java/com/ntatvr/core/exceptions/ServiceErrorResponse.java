package com.ntatvr.core.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceErrorResponse {

    private Date time;

    private String errorCode;

    private String internalMessage;

    private String userMessage;

    private List<ValidationErrorResponse> errors;

    public ServiceErrorResponse(final ErrorEnum errorEnum) {
        this.time = new Date();
        this.errorCode = errorEnum.getCode();
        this.internalMessage = errorEnum.getMessage();
        this.userMessage = errorEnum.getUserMessage();
    }

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
