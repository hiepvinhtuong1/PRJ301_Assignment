package com.hipdev.LeaveManagement.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
/*
@JsonIdentityInfo để đánh dấu các đối tượng với một ID duy nhất.
 Khi Jackson gặp một đối tượng đã được serialize, nó sẽ không serialize lại mà chỉ tham chiếu đến ID của đối tượng đó.*/
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
