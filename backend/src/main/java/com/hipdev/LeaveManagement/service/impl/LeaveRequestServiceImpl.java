package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.entity.User;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    public Page<LeaveRequestDTO> getLeaveRequestByUsername(int page, int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var username = authentication.getName();
        var existedUser = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        if (existedUser.getEmployee() == null || existedUser.getEmployee().getCreatedRequests() == null) {
            return new PageImpl<>(List.of(), PageRequest.of(page, size), 0); // Trả về trang rỗng
        }

        // Lấy danh sách yêu cầu nghỉ phép
        List<LeaveRequest> leaveRequests = existedUser.getEmployee().getCreatedRequests();
        List<LeaveRequestDTO> leaveRequestDTOs = leaveRequestMapper.toDtos(leaveRequests);

        // Tạo phân trang thủ công
        int start = Math.min((int) PageRequest.of(page, size).getOffset(), leaveRequestDTOs.size());
        int end = Math.min(start + size, leaveRequestDTOs.size());
        List<LeaveRequestDTO> pagedList = leaveRequestDTOs.subList(start, end);

        return new PageImpl<>(pagedList, PageRequest.of(page, size), leaveRequestDTOs.size());
    }


    @Override
    public LeaveRequestDTO createLeaveRequest(CreateLeaveRequest request) {
        if (Objects.isNull(request.getStartDate()) || Objects.isNull(request.getEndDate())) {
            throw new AppException(ErrorCode.LEAVE_REQUEST_DATE_NULL);
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.LEAVE_REQUEST_INVALID_DATE_RANGE);
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User existedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .creator(existedUser.getEmployee())
                .status(LeaveRequest.Status.Pending)
                .build();
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        return leaveRequestMapper.toDto(leaveRequest);
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
