package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.entity.User;
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
            if (leaveRequest.getStatus() == null) {
                leaveRequest.setStatus("PENDING");
            }
            leaveRequestRepository.save(leaveRequest);
            response.setStatusCode(200);
            response.setMessage("success");
        } catch (MyException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
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

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getLeaveRequestById(Long requestId, Long currentUserId) {
        Response response = new Response();

        try {
            LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId).orElseThrow(
                    () -> new MyException("Leave request not found")
            );
            LeaveRequestDTO leaveRequestDTO = Utils.mapLeaveRequestEntityToDTO(leaveRequest);

            if (leaveRequest.getCreator().getId().equals(currentUserId)) {
                response.setStatusCode(200);
                response.setMessage("Success");

                response.setLeaveRequest(leaveRequestDTO);
                return response;
            }

            User creator = leaveRequest.getCreator();
            User leader = creator.getLeader();
            if (leader != null && leader.getId().equals(currentUserId)) {
                response.setStatusCode(200);
                response.setMessage("Success");
                response.setLeaveRequest(leaveRequestDTO);
                return response;
            }

            if (creator.getDepartment() != null && creator.getDepartment().getManage().getId().equals(currentUserId)) {
                response.setStatusCode(200);
                response.setMessage("Success");
                response.setLeaveRequest(leaveRequestDTO);
                return response;
            }

        } catch (MyException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getLeaveRequestById: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response processRequest(LeaveRequest updatedRequest, Long processerId, Long requestId) {
        Response response = new Response();

        try {
            // Tìm yêu cầu nghỉ trong cơ sở dữ liệu
            LeaveRequest existingLeaveRequest = leaveRequestRepository.findById(requestId).orElseThrow(
                    () -> new MyException("Request not found")
            );

            // Kiểm tra người dùng xử lý là Leader hoặc Manager của người tạo yêu cầu
            User creator = existingLeaveRequest.getCreator();
            User leader = creator.getLeader();
            if (!creator.getId().equals(processerId) &&
                    !processerId.equals(leader.getId()) &&
                    !creator.getDepartment().getManage().getId().equals(processerId)) {
                response.setStatusCode(403);
                response.setMessage("You are not authorized to process this request");
                return response;
            }

            // Cập nhật trạng thái yêu cầu
            existingLeaveRequest.setStatus(updatedRequest.getStatus()); // Approved or Rejected
            existingLeaveRequest.setComment(updatedRequest.getComment());
            existingLeaveRequest.setProcessor(userRepository.findById(processerId).orElseThrow(() -> new MyException("User not found")));

            // Lưu thay đổi
            leaveRequestRepository.save(existingLeaveRequest);

            response.setStatusCode(200);
            response.setMessage("Request processed successfully");
        } catch (MyException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error processing request: " + e.getMessage());
        }

        return response;
    }


    public Response updateLeaveRequest(LeaveRequest updatedRequest, Long userId, Long requestId) {
        Response response = new Response();

        try {
            // Lấy thông tin yêu cầu nghỉ phép từ database
            var exsitedLeaveRequest = leaveRequestRepository.findById(requestId).orElseThrow(
                    () -> new MyException("Request not found")
            );

            // Kiểm tra xem người dùng hiện tại có phải là người tạo yêu cầu này không
            if (!exsitedLeaveRequest.getCreator().getId().equals(userId)) {
                throw new MyException("You are not the owner of this request");
            }

            // Kiểm tra trạng thái của yêu cầu nghỉ phép
            if (exsitedLeaveRequest.getStatus().equals("Approved")) {
                throw new MyException("Approved request cannot be updated");
            }

            if (exsitedLeaveRequest.getStatus().equals("Rejected")) {
                throw new MyException("Rejected request cannot be updated");
            }

            // Kiểm tra tính hợp lệ của ngày bắt đầu và kết thúc
            if (updatedRequest.getEndDate().isBefore(updatedRequest.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }

            // Cập nhật thông tin yêu cầu nghỉ phép
            exsitedLeaveRequest.setEndDate(updatedRequest.getEndDate());
            exsitedLeaveRequest.setStartDate(updatedRequest.getStartDate());
            exsitedLeaveRequest.setReason(updatedRequest.getReason());

            leaveRequestRepository.save(exsitedLeaveRequest);
            response.setStatusCode(200);
            response.setMessage("Request updated successfully");
        } catch (MyException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating request: " + e.getMessage());
        }

        return response;
    }


    @Override
    public Response deleteLeaveRequest(Long id, Long userId) {
        Response response = new Response();

        try {
            // Lấy thông tin yêu cầu nghỉ phép từ database
            var exsitedLeaveRequest = leaveRequestRepository.findById(id).orElseThrow(
                    () -> new MyException("Request not found")
            );

            // Kiểm tra xem người dùng hiện tại có phải là người tạo yêu cầu này không
            if (!exsitedLeaveRequest.getCreator().getId().equals(userId)) {
                throw new MyException("You are not the owner of this request");
            }

            // Kiểm tra trạng thái của yêu cầu nghỉ phép
            if (exsitedLeaveRequest.getStatus().equals("Approved")) {
                throw new MyException("Approved request cannot be deleted");
            }

            if (exsitedLeaveRequest.getStatus().equals("Rejected")) {
                throw new MyException("Rejected request cannot be deleted");
            }

            // Xóa yêu cầu nghỉ phép
            leaveRequestRepository.delete(exsitedLeaveRequest);
            response.setStatusCode(200);
            response.setMessage("Request deleted successfully");
        } catch (MyException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting request: " + e.getMessage());
        }

        return response;
    }


}
