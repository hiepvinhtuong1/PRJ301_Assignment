package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.exception.MyException;
import com.hipdev.LeaveManagement.repo.LeaveRequestRepository;
import com.hipdev.LeaveManagement.repo.UserRepository;
import com.hipdev.LeaveManagement.service.interfac.ILeaveRequestService;
import com.hipdev.LeaveManagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Response response = new Response();

        try {
            List<LeaveRequest> requests = leaveRequestRepository.findAll();
            List<LeaveRequestDTO> requestDTOs = Utils.mapRequestListEntityToListDTO(requests);

            response.setLeaveRequests(requestDTOs);
            response.setStatusCode(200);
            response.setMessage("success");

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getLeaveRequestById(Long id) {
        Response response = new Response();

        try {
            LeaveRequest leaveRequest = leaveRequestRepository.findById(id).orElseThrow(
                    () -> new MyException("Leave request not found")
            );

            LeaveRequestDTO leaveRequestDTO = Utils.mapLeaveRequestEntityToDTO(leaveRequest);
            response.setLeaveRequest(leaveRequestDTO);
            response.setStatusCode(200);
            response.setMessage("success");

        } catch (MyException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getLeaveRequestById: "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response processRequest(LeaveRequest updatedRequest, Long processerId) {
        Response response = new Response();

        try {
            var exsitedLeaveRequest = leaveRequestRepository.findById(updatedRequest.getId()).orElseThrow(
                    () -> new MyException("Request not found")
            );

            var processor = userRepository.findById(processerId).orElseThrow(
                    () -> new MyException("User not found")
            );

            exsitedLeaveRequest.setStatus(updatedRequest.getStatus());
            exsitedLeaveRequest.setComment(updatedRequest.getComment());
            exsitedLeaveRequest.setProcessor(processor);

            leaveRequestRepository.save(exsitedLeaveRequest);
            response.setStatusCode(200);
            response.setMessage("success");
        }catch (MyException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error processing a request: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateLeaveRequest(LeaveRequest updatedRequest, Long userId) {
        Response response = new Response();

        try {
            var exsitedLeaveRequest = leaveRequestRepository.findById(updatedRequest.getId()).orElseThrow(
                    () -> new MyException("Request not found")
            );

            var exsitUser = userRepository.findById(userId).orElseThrow(
                    () -> new MyException("User not found")
            );

            if (exsitedLeaveRequest.getStatus().equals("Approved")) {
                throw new MyException("Approved request cannot be updated");
            }

            if (exsitedLeaveRequest.getStatus().equals("Rejected")) {
                throw new MyException("Rejected request cannot be updated");
            }

            if (exsitUser.getLeaveRequests() != null) {
                if (!exsitUser.getLeaveRequests().contains(exsitedLeaveRequest)) {
                    throw new MyException("User dont have the same leave request");
                }

                if (updatedRequest.getEndDate().isBefore(updatedRequest.getStartDate())) {
                    throw new IllegalArgumentException("End date cannot be before start date");
                }

                exsitedLeaveRequest.setEndDate(updatedRequest.getEndDate());
                exsitedLeaveRequest.setStartDate(updatedRequest.getStartDate());
                exsitedLeaveRequest.setReason(updatedRequest.getReason());

                leaveRequestRepository.save(exsitedLeaveRequest);
                response.setStatusCode(200);
                response.setMessage("success");
            } else {
                throw new MyException("User has never have leave request");
            }
        }catch (MyException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error update a request: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteLeaveRequest(Long id, Long userId) {
        Response response = new Response();

        try {
            var exsitedLeaveRequest = leaveRequestRepository.findById(id).orElseThrow(
                    () -> new MyException("Request not found")
            );

            var exsitUser = userRepository.findById(userId).orElseThrow(
                    () -> new MyException("User not found")
            );

            if (exsitedLeaveRequest.getStatus().equals("Approved")) {
                throw new MyException("Approved request cannot be deleted");
            }

            if (exsitedLeaveRequest.getStatus().equals("Rejected")) {
                throw new MyException("Rejected request cannot be deleted");
            }

            if (exsitUser.getLeaveRequests() != null) {
                if (!exsitUser.getLeaveRequests().contains(exsitedLeaveRequest)) {
                    throw new MyException("User dont have the same leave request");
                }

                leaveRequestRepository.delete(exsitedLeaveRequest);
                response.setStatusCode(200);
                response.setMessage("success");
            } else {
                throw new MyException("User has never have leave request");
            }
        }catch (MyException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error delete a request: " + e.getMessage());
        }
        return response;
    }

}
