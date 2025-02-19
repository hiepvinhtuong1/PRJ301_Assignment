package com.hipdev.LeaveManagement.service.interfac;

import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.LeaveRequest;

public interface ILeaveRequestService {

    Response saveRequest(LeaveRequest leaveRequest, Long userId);

    Response getAllRequests();

    Response processRequest(Long id);
}
