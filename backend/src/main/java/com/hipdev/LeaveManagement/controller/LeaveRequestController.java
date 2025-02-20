package com.hipdev.LeaveManagement.controller;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.entity.User;
import com.hipdev.LeaveManagement.service.impl.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping("/save")
    public ResponseEntity<Response> saveLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            var userId = userDetails.getId();
            leaveRequest.setCreator(userDetails);
            Response response = leaveRequestService.saveRequest(leaveRequest,userId);
            return ResponseEntity.status(200).body(response);
        }
        Response response = new Response();
        response.setStatusCode(403);
        response.setMessage("You have to be logged in");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllLeaveRequests() {
        Response response = leaveRequestService.getAllRequests();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-request-by-id/{requestId}")
    public ResponseEntity<Response> getLeaveRequestById(@PathVariable Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            Long currentUserId = userDetails.getId();
            Response response = leaveRequestService.getLeaveRequestById(requestId, currentUserId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = new Response();
        response.setStatusCode(403);
        response.setMessage("You have to be logged in");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/process-request/{requestId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LEADER', 'MANAGER')")
    public ResponseEntity<Response> processLeaveRequest(@PathVariable Long requestId, @RequestBody LeaveRequest updatedRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            Long currentUserId = userDetails.getId();

            // Gọi service để xử lý yêu cầu
            Response response = leaveRequestService.processRequest(updatedRequest, currentUserId, requestId);

            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = new Response();
        response.setStatusCode(403);
        response.setMessage("You need to be logged in.");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-request/{requestId}")
    public ResponseEntity<Response> updateLeaveRequest(@RequestBody LeaveRequest updatedRequest, @PathVariable Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            Long currentUserId = userDetails.getId();

            // Kiểm tra xem người dùng hiện tại có phải là người tạo yêu cầu này không
            Response response = leaveRequestService.updateLeaveRequest(updatedRequest, currentUserId, requestId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = new Response();
        response.setStatusCode(403);
        response.setMessage("You need to be logged in.");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete-request/{requestId}")
    public ResponseEntity<Response> deleteLeaveRequest(@PathVariable Long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            Long currentUserId = userDetails.getId();

            // Kiểm tra xem người dùng hiện tại có phải là người tạo yêu cầu này không
            Response response = leaveRequestService.deleteLeaveRequest(requestId, currentUserId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = new Response();
        response.setStatusCode(403);
        response.setMessage("You need to be logged in.");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
