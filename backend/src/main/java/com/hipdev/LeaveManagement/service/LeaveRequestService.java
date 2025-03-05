package com.hipdev.LeaveManagement.service;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;

import java.util.List;

public interface LeaveRequestService {
    public List<LeaveRequestDTO> getAllLeaveRequests();
    public LeaveRequestDTO getLeaveRequestById(Long id);
    public List<LeaveRequestDTO> getLeaveRequestByUsername();
    public LeaveRequestDTO createLeaveRequest(CreateLeaveRequest request);
    public LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequestDTO);
    public Void deleteLeaveRequest(Long leaveRequestId);
}
