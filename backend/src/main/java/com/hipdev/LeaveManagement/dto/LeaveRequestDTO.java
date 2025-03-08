package com.hipdev.LeaveManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveRequestDTO {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String reason;
    private String comment;
    private EmployeeDTO creator;
    private EmployeeDTO processor;
}
