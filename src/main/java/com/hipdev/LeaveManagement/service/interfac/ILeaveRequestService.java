package com.hipdev.LeaveManagement.service.interfac;

import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.LeaveRequest;

public interface ILeaveRequestService {

    Response saveRequest(LeaveRequest leaveRequest, Long userId);

    Response getAllRequests();

    Response getLeaveRequestById(Long requestId, Long currentUserId);

    Response processRequest(LeaveRequest updatedRequest, Long processerId, Long requestId);

    Response updateLeaveRequest(LeaveRequest updatedRequest, Long userId, Long requestId);

    Response deleteLeaveRequest(Long id, Long userId);
}
