package com.hipdev.LeaveManagement.service;


import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.dto.response.ApiResponse;

import java.util.List;

public interface UserService {
    public List<UserDTO> getAllUsers();
    public UserDTO getUserById(Long userId);
    public UserDTO getMyInfo(UserDTO userDTO);
    public UserDTO updateUser(UserDTO userDTO);
    public Void deleteUser(Long userId);
}
