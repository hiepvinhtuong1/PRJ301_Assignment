package com.hipdev.LeaveManagement.controller;


import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.dto.response.ApiResponse;
import com.hipdev.LeaveManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers(){
        var result = userService.getAllUsers();
        return ApiResponse.<List<UserDTO>>builder()
                .code(200)
                .message("Success")
                .data(result)
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long userId){
        var result = userService.getUserById(userId);
        return ApiResponse.<UserDTO>builder()
                .code(200)
                .message("Success")
                .data(result)
                .build();
    }

   // public ApiResponse<UserDTO> addUser(UserDTO userDTO){}
}
