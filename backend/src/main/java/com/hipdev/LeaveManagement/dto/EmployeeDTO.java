package com.hipdev.LeaveManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO {
    private Integer employeeId;

    private String fullName;

    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private String gender;
}
