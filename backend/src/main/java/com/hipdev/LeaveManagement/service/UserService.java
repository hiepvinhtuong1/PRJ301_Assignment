package com.hipdev.LeaveManagement.service;


import com.hipdev.LeaveManagement.dto.EmployeeDTO;
import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.dto.response.ApiResponse;

import java.util.List;

public interface UserService {
    public List<UserDTO> getAllUsers();
    public UserDTO getUserById(String userId);
    public UserDTO getMyInfo();
    public UserDTO updateUser(UserDTO userDTO, String userId);
    public Void deleteUser(String userId);
}
