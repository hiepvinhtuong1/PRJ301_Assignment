package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.FilterLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.UpdateLeaveRequest;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.entity.User;
import com.hipdev.LeaveManagement.exception.AppException;
import com.hipdev.LeaveManagement.exception.ErrorCode;
import com.hipdev.LeaveManagement.mapper.LeaveRequestMapper;
import com.hipdev.LeaveManagement.repository.LeaveRequestRepository;
import com.hipdev.LeaveManagement.repository.UserRepository;
import com.hipdev.LeaveManagement.service.LeaveRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    @PreAuthorize("hasAuthority('READ_REQUEST')")
    public Page<LeaveRequestDTO> getYourOwnRequest(int page, int size, FilterLeaveRequest filterLeaveRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var username = authentication.getName();
        var existedUser = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        if (existedUser.getEmployee() == null) {
            return new PageImpl<>(List.of(), PageRequest.of(page, size), 0); // Trả về trang rỗng nếu không có Employee
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());



        Page<LeaveRequest> leaveRequests = leaveRequestRepository.findByFilters(
                filterLeaveRequest.getStatus(),
                filterLeaveRequest.getStartDate(),
                filterLeaveRequest.getEndDate(),
                existedUser.getEmployee().getEmployeeId(),
                filterLeaveRequest.getProcessorId(),
                pageable);


        return leaveRequests.map(leaveRequestMapper::toDto);
    }


    @Override
    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    public LeaveRequestDTO createLeaveRequest(CreateLeaveRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User existedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (Objects.isNull(request.getStartDate()) || Objects.isNull(request.getEndDate())) {
            throw new AppException(ErrorCode.LEAVE_REQUEST_DATE_NULL);
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.LEAVE_REQUEST_INVALID_DATE_RANGE);
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .creator(existedUser.getEmployee())
                .status("PENDING")
                .build();
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        return leaveRequestMapper.toDto(leaveRequest);
    }

    @Override
    @PreAuthorize("hasAuthority('UPDATE_REQUEST')")
    public LeaveRequestDTO updateLeaveRequest(UpdateLeaveRequest request) {
        validateUpdateRequest(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var existedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        LeaveRequest  existedLeaveRequest = leaveRequestRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.LEAVEREQUEST_NOT_FOUND));

        existedLeaveRequest.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .build();
        LeaveRequest leaveRequest = leaveRequestRepository.save(existedLeaveRequest);

        return leaveRequestMapper.toDto(leaveRequest);
    }

    @Override
    public Void deleteLeaveRequest(Long leaveRequestId) {
        return null;
    }

    private void validateUpdateRequest(UpdateLeaveRequest request) {
        // Kiểm tra các trường bắt buộc
        if (request.getId() == null ) {
            throw new IllegalArgumentException("Id cannot be empty");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be empty");
        }
        if (request.getStartDate() == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (request.getEndDate() == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        // Kiểm tra startDate phải trước endDate
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Kiểm tra startDate không được trước ngày hiện tại
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
    }
}
