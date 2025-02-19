package com.hipdev.LeaveManagement.controller;

import com.hipdev.LeaveManagement.dto.LoginRequest;
import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.User;
import com.hipdev.LeaveManagement.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }
}
