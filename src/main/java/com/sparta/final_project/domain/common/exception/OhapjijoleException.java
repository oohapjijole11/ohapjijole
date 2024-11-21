package com.sparta.final_project.domain.common.exception;

import lombok.Getter;

@Getter
public class OhapjijoleException extends RuntimeException {

    private ErrorCode errorCode;

    public OhapjijoleException(ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.errorCode = errorcode;
    }

    public OhapjijoleException(ErrorCode errorcode, String plusmessage) {
        super(errorcode.getMessage() + plusmessage);
        this.errorCode = errorcode;
    }
}
