package com.hipdev.LeaveManagement.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class FilterLeaveRequest {
    private String status = "";
    private LocalDate startDate;
    private LocalDate endDate;
    private String fullname = "";
    private Integer processorId ;
}