package com.hipdev.LeaveManagement.controller;

import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.service.CustomUserDetailService;
import com.hipdev.LeaveManagement.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority(ADMIN)")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-id/{userId}")
    @PreAuthorize("hasAnyAuthority(ADMIN,LEADER,MANAGER)")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Response response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-my-info/")
    public ResponseEntity<Response> getMyInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Response response = userService.getMyInfo(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId) {
        Response response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-user-request/{userId}")
    public ResponseEntity<Response> getUserRequest(@PathVariable("userId") String userId) {
        Response response = userService.getUserRequestHistory(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
