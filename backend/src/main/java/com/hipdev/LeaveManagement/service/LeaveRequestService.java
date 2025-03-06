package com.hipdev.LeaveManagement.service;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.FilterLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LeaveRequestService {
    public List<LeaveRequestDTO> getAllLeaveRequests();
    public LeaveRequestDTO getLeaveRequestById(Long id);
    public Page<LeaveRequestDTO> getYourOwnRequest(int page, int size, FilterLeaveRequest filterLeaveRequest);
    public LeaveRequestDTO createLeaveRequest(CreateLeaveRequest request);
    public LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequestDTO);
    public Void deleteLeaveRequest(Long leaveRequestId);
}
