package com.hipdev.LeaveManagement.service.interfac;

import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.LeaveRequest;

public interface ILeaveRequestService {

    Response saveRequest(LeaveRequest leaveRequest, Long userId);

    Response getAllRequests();

    Response getLeaveRequestById(Long id);

    Response processRequest(LeaveRequest updatedRequest, Long processerId);

    Response updateLeaveRequest(LeaveRequest updatedRequest, Long userId);
}
