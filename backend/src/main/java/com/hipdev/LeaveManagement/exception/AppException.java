package com.hipdev.LeaveManagement.exception;

public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
