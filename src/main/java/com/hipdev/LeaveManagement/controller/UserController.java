package com.hipdev.LeaveManagement.controller;

import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.User;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-id/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','LEADER','MANAGER')")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (!userService.isLeaderOrMangerOfUser(userId, username)) {
            Response response = new Response();
            response.setStatusCode(403);
            response.setMessage("You are not the leader or master of the user");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-my-info")
    public ResponseEntity<Response> getMyInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Response response = userService.getMyInfo(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyAuthority(ADMIN,MANAGER,LEADER)")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (!userService.isLeaderOrMangerOfUser(userId, username)) {
            Response response = new Response();
            response.setStatusCode(403);
            response.setMessage("You are not the leader or master of the user");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-user-request/{userId}")
    @PreAuthorize("hasAnyAuthority(ADMIN,MANAGER,LEADER)")
    public ResponseEntity<Response> getUserRequestHistory(@PathVariable("userId") String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (!userService.isLeaderOrMangerOfUser(userId, username)) {
            Response response = new Response();
            response.setStatusCode(403);
            response.setMessage("You are not the leader or master of the user");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = userService.getUserRequestHistory(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/get-users-by-role")
    @PreAuthorize("hasAnyAuthority('ADMIN','LEADER','MANAGER')")
    public ResponseEntity<Response> getUsersByRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Response response = userService.getUserByRole(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
