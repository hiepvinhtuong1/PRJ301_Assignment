package com.hipdev.LeaveManagement.service;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.FilterLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.UpdateLeaveRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LeaveRequestService {
     List<LeaveRequestDTO> getAllLeaveRequests();
     LeaveRequestDTO getLeaveRequestById(Long id);
     Page<LeaveRequestDTO> getYourOwnRequest(int page, int size, FilterLeaveRequest filterLeaveRequest);
     LeaveRequestDTO createLeaveRequest(CreateLeaveRequest request);
     LeaveRequestDTO updateLeaveRequest(UpdateLeaveRequest leaveRequestDTO, Long id);
     Void deleteLeaveRequest(Long leaveRequestId);
     Page<LeaveRequestDTO> getEmployeeRequestsAfterToday(int page, int size);
     LeaveRequestDTO processLeaveRequest(Long leaveRequestId, UpdateLeaveRequest updateLeaveRequest);
}
