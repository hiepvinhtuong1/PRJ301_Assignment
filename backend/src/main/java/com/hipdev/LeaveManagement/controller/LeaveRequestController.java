package com.hipdev.LeaveManagement.controller;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import com.hipdev.LeaveManagement.dto.response.ApiResponse;
import com.hipdev.LeaveManagement.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaveRequestDTO>>> getLeaveRequestByUsername() {
        List<LeaveRequestDTO> list = leaveRequestService.getLeaveRequestByUsername();
        return ResponseEntity.ok(ApiResponse.<List<LeaveRequestDTO>>builder()
                .message("List of leave request by username")
                .data(list)
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
