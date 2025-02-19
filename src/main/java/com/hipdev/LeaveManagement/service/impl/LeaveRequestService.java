package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.exception.MyException;
import com.hipdev.LeaveManagement.repo.LeaveRequestRepository;
import com.hipdev.LeaveManagement.repo.UserRepository;
import com.hipdev.LeaveManagement.service.interfac.ILeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestService implements ILeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response saveRequest(LeaveRequest leaveRequest, Long userId) {
        Response response = new Response();

        try {
            if (leaveRequest.getEndDate().isBefore(leaveRequest.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }

            var user = userRepository.findById(userId).orElseThrow(
                    () -> new MyException("User not found")
            );

            leaveRequestRepository.save(leaveRequest);
            response.setStatusCode(200);
            response.setMessage("success");
        }catch (MyException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a request: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllRequests() {
        return null;
    }

    @Override
    public Response processRequest(Long id) {
        return null;
    }
}
