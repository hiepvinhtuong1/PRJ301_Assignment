package com.hipdev.LeaveManagement.service;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;

import java.util.List;

public interface LeaveRequestService {
    public List<LeaveRequestDTO> getAllLeaveRequests();
    public LeaveRequestDTO getLeaveRequestById(Long id);
    public List<LeaveRequestDTO> getLeaveRequestByUsername(String username);
    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO);
    public LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequestDTO);
    public Void deleteLeaveRequest(Long leaveRequestId);
}
