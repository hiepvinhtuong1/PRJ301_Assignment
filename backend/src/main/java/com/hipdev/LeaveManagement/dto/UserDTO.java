package com.hipdev.LeaveManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Integer userId;
    private String username;
 //   private DepartmentDTO department;
    private UserDTO leader;
    private EmployeeDTO employee;
    private List<RoleDTO> roles;
    private List<LeaveRequestDTO> leaveRequests;
    private List<LeaveRequestDTO> processedLeaveRequests;
}
