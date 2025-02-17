package com.hipdev.LeaveManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Response {

    private int statusCode;
    private String message;
    private String token;
    private String role;
    private String expirationTime;
    private UserDTO user;
    private DepartmentDTO department;
    private List<UserDTO> users;
    private List<LeaveRequestDTO> leaveRequests;
}
