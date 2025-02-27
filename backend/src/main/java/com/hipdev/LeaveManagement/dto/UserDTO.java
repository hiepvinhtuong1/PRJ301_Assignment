package com.hipdev.LeaveManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String username;
    private String fullname;
    private DepartmentDTO department;
    private UserDTO leader;
    private List<RoleDTO> roles;
    private List<LeaveRequestDTO> leaveRequests;
    private List<LeaveRequestDTO> processedLeaveRequests;
}
