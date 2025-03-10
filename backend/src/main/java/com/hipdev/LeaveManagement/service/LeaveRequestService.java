package com.hipdev.LeaveManagement.service;

import com.hipdev.LeaveManagement.dto.EmployeeDTO;
import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.FilterLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.UpdateLeaveRequest;
import com.hipdev.LeaveManagement.dto.response.CalendarResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestService {
     List<LeaveRequestDTO> getAllLeaveRequests();
     LeaveRequestDTO getLeaveRequestById(Long id);
     Page<LeaveRequestDTO> getYourOwnRequest(int page, int size, FilterLeaveRequest filterLeaveRequest);
     LeaveRequestDTO createLeaveRequest(CreateLeaveRequest request);
     LeaveRequestDTO updateLeaveRequest(UpdateLeaveRequest leaveRequestDTO, Long id);
     Void deleteLeaveRequest(Long leaveRequestId);
     Page<LeaveRequestDTO> getEmployeeRequestsAfterToday(int page, int size, FilterLeaveRequest filterLeaveRequest);
     LeaveRequestDTO processLeaveRequest(Long leaveRequestId, UpdateLeaveRequest updateLeaveRequest);
     List<EmployeeDTO> getEmployeesOfLeader();
     List<CalendarResponse> getCalendar(LocalDate startDate, LocalDate endDate);
}
