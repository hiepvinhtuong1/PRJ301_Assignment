package com.hipdev.LeaveManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hipdev.LeaveManagement.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveRequestDTO {
    private Long id;

    private String reason;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    private String comment;

    private UserDTO creator;

    private UserDTO processor;
}
