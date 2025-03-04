package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.exception.AppException;
import com.hipdev.LeaveManagement.exception.ErrorCode;
import com.hipdev.LeaveManagement.mapper.LeaveRequestMapper;
import com.hipdev.LeaveManagement.mapper.UserMapper;
import com.hipdev.LeaveManagement.repository.LeaveRequestRepository;
import com.hipdev.LeaveManagement.repository.UserRepository;
import com.hipdev.LeaveManagement.service.LeaveRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveRequestServiceImpl implements LeaveRequestService {
    final LeaveRequestRepository leaveRequestRepository;
    final UserRepository userRepository;
    final LeaveRequestMapper leaveRequestMapper;

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        List<LeaveRequestDTO> leaveRequestDTOs = leaveRequestMapper.toDtos(leaveRequests);
        return leaveRequestDTOs;
    }

    @Override
    public LeaveRequestDTO getLeaveRequestById(Long id) {
        var existedLeaveRequest = leaveRequestRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.LEAVEREQUEST_NOT_FOUND)
        );

        return leaveRequestMapper.toDto(existedLeaveRequest);
    }

    @Override
    public List<LeaveRequestDTO> getLeaveRequestByUsername(String username) {
        var existedUser = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        if (existedUser.getEmployee() != null &&
                existedUser.getEmployee().getCreatedRequests() != null) {
            leaveRequests = existedUser.getEmployee().getCreatedRequests();
        }
        return leaveRequestMapper.toDtos(leaveRequests);
    }

    @Override
    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO) {

        return null;
    }

    @Override
    public LeaveRequestDTO updateLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        return null;
    }

    @Override
    public Void deleteLeaveRequest(Long leaveRequestId) {
        return null;
    }
}
