package com.hipdev.LeaveManagement.controller;

import com.hipdev.LeaveManagement.dto.EmployeeDTO;
import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.FilterLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.UpdateLeaveRequest;
import com.hipdev.LeaveManagement.dto.response.ApiResponse;
import com.hipdev.LeaveManagement.dto.response.CalendarResponse;
import com.hipdev.LeaveManagement.entity.Employee;
import com.hipdev.LeaveManagement.service.LeaveRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LeaveRequestDTO>>> getLeaveRequestByUsername(
            @ModelAttribute FilterLeaveRequest filterLeaveRequest,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<LeaveRequestDTO> leaveRequests = leaveRequestService.getYourOwnRequest(page - 1, size, filterLeaveRequest);
        return ResponseEntity.ok(ApiResponse.<Page<LeaveRequestDTO>>builder()
                .message("List of leave requests by username with pagination")
                .data(leaveRequests)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> createLeaveRequest(
            @RequestBody @Valid CreateLeaveRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LeaveRequestDTO leaveRequestDTO = leaveRequestService.createLeaveRequest(request);

        return ResponseEntity.ok(ApiResponse.<LeaveRequestDTO>builder()
                .message("Leave request created successfully")
                .data(leaveRequestDTO)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> updateLeaveRequest(
            @RequestBody @Valid UpdateLeaveRequest request
            , @PathVariable Long id) {
        LeaveRequestDTO leaveRequestDTO = leaveRequestService.updateLeaveRequest(request, id);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestDTO>builder()
                .message("Leave request updated successfully")
                .data(leaveRequestDTO)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("req-of-emp")
    public ResponseEntity<ApiResponse<Page<LeaveRequestDTO>>> getLeaveRequestByEmp(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute FilterLeaveRequest filterLeaveRequest
    ) {
        Page<LeaveRequestDTO> list = leaveRequestService.getEmployeeRequestsAfterToday(page - 1, size, filterLeaveRequest);
        return ResponseEntity.ok(ApiResponse.<Page<LeaveRequestDTO>>builder()
                .message("List of leave requests by username with pagination")
                .data(list)
                .build());
    }

    @PutMapping("/process/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestDTO>> processLeaveRequest(
            @PathVariable Long id,
            @RequestBody UpdateLeaveRequest updateLeaveRequest

    ) {
        LeaveRequestDTO requestDTO = leaveRequestService.processLeaveRequest(id, updateLeaveRequest);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestDTO>builder()
                .message("Leave request processed successfully")
                .build());
    }

    @GetMapping("/employees")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getLeaveRequestByEmploy(
    ) {
        List<EmployeeDTO> response = leaveRequestService.getEmployeesOfLeader();
        return ResponseEntity.ok(ApiResponse.<List<EmployeeDTO>>builder()
                .data(response)
                .message("Get employees of leader successfully")
                .build());
    }

    @GetMapping("/calendar")
    public ResponseEntity<ApiResponse<List<CalendarResponse>>> getCalendar(
            @RequestParam(defaultValue = "") LocalDate startDate,
            @RequestParam(defaultValue = "") LocalDate endDate
    ) {
        List<CalendarResponse> response = leaveRequestService.getCalendar(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.<List<CalendarResponse>>builder()
                .data(response)
                .message("Get calendar successfully")
                .build());
    }
}
