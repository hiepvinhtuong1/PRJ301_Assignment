package com.hipdev.LeaveManagement.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1002, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXISTED(1003, "User not exsited", HttpStatus.NOT_FOUND),
    USER_EXISTED(1004, "User is already taken", HttpStatus.BAD_REQUEST),
    ;
    final private int code;
    final private String message;
    final private HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
