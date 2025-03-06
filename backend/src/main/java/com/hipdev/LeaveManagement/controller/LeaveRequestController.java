package com.hipdev.LeaveManagement.controller;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import com.hipdev.LeaveManagement.dto.response.ApiResponse;
import com.hipdev.LeaveManagement.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LeaveRequestDTO>>> getLeaveRequestByUsername(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<LeaveRequestDTO> leaveRequests = leaveRequestService.getLeaveRequestByCreatorId(page-1, size);
        return ResponseEntity.ok(ApiResponse.<Page<LeaveRequestDTO>>builder()
                .message("List of leave requests by username with pagination")
                .data(leaveRequests)
                .build());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> createLeaveRequest(
            @RequestBody @Valid CreateLeaveRequest request) {
        LeaveRequestDTO leaveRequestDTO = leaveRequestService.createLeaveRequest(request);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestDTO>builder()
                .message("Leave request created successfully")
                .data(leaveRequestDTO)
                .build());
    }
}
