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
    LEAVEREQUEST_NOT_FOUND(1005, "Leave Request Not Found", HttpStatus.BAD_REQUEST),
    LEAVEREQUEST_EXISTED(1006, "Leave Request Already Existed", HttpStatus.BAD_REQUEST),
    LEAVE_REQUEST_DATE_NULL(1007,"Leave Request Date Null" , HttpStatus.BAD_REQUEST ),
    LEAVE_REQUEST_INVALID_DATE_RANGE(1008,"Leave Request Invalid Date Range" , HttpStatus.BAD_REQUEST ),
    UNAUTHORIZED_ACCESS(1009, "Unauthorized access" , HttpStatus.UNAUTHORIZED ),
    LEAVEREQUEST_CANNOT_UPDATE(1010, "Cannot update leave request ", HttpStatus.BAD_REQUEST),
    LEAVEREQUEST_CANNOT_DELETE(1011, "Cannot delete leave request " , HttpStatus.BAD_REQUEST ),
    REQUEST_NOT_BELONG_YOU(1012,"Request Not Belong You" , HttpStatus.BAD_REQUEST ),
    USER_NOT_EMPLOYEE(1013,"User Not Employee" , HttpStatus.BAD_REQUEST ),
    UNAUTHORIZED_PROCESS_REQUEST(1014,"Unauthorized process request" , HttpStatus.UNAUTHORIZED ),
    INVALID_STATUS(1015,"Invalid status" , HttpStatus.BAD_REQUEST ),
    LEAVEREQUEST_CANNOT_PROCESS(1016,"Cannot process leave request" , HttpStatus.BAD_REQUEST ),
    LEAVE_REQUEST_START_DATE_BEFORE_CURRENT(1017,"Leave Request Start Date before current date" , HttpStatus.BAD_REQUEST ),
    LEAVE_REQUEST_OVERLAP(1018,"Leave Request Overlap" , HttpStatus.BAD_REQUEST ),;
    final private int code;
    final private String message;
    final private HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
