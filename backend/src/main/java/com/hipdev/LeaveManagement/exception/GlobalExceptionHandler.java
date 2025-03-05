package com.hipdev.LeaveManagement.exception;

import com.hipdev.LeaveManagement.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e) {
        log.error("AppException occurred: {}", e.getErrorCode().getMessage(), e);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(e.getErrorCode().getCode());
        apiResponse.setMessage(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleGenericException(Exception exception) {
        log.error("Unexpected error occurred: {}", exception.getMessage(), exception);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatus()).body(apiResponse);
    }
}
