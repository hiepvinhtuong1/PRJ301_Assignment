package com.hipdev.LeaveManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hipdev.LeaveManagement.entity.Department;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;

    private String username;

    private String fullName;

    private String role;

    private UserDTO leader;

    private DepartmentDTO department;

    private List<LeaveRequestDTO> leaveRequests = new ArrayList<>();
}
