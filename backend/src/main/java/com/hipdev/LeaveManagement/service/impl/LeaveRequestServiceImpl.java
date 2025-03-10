package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.EmployeeDTO;
import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.request.FilterLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.CreateLeaveRequest;
import com.hipdev.LeaveManagement.dto.request.leave_request.UpdateLeaveRequest;
import com.hipdev.LeaveManagement.dto.response.CalendarResponse;
import com.hipdev.LeaveManagement.entity.Employee;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.entity.User;
import com.hipdev.LeaveManagement.exception.AppException;
import com.hipdev.LeaveManagement.exception.ErrorCode;
import com.hipdev.LeaveManagement.mapper.EmployeeMapper;
import com.hipdev.LeaveManagement.mapper.LeaveRequestMapper;
import com.hipdev.LeaveManagement.repository.EmployeeRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveRequestServiceImpl implements LeaveRequestService {
    final LeaveRequestRepository leaveRequestRepository;
    final UserRepository userRepository;
    final LeaveRequestMapper leaveRequestMapper;
    final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

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

        // Kiểm tra ngày bắt đầu phải lớn hơn hoặc bằng ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        if (request.getStartDate().isBefore(currentDate)) {
            throw new AppException(ErrorCode.LEAVE_REQUEST_START_DATE_BEFORE_CURRENT);
        }

        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").descending());


        List<LeaveRequest> approvedRequests = leaveRequestRepository.findByCreatorAndStatusAndEndDateGreaterThanEqual(
                existedUser.getEmployee(),
                "Approved".toUpperCase(),
                request.getStartDate()
        );

        if (!approvedRequests.isEmpty()) {
            throw new AppException(ErrorCode.LEAVE_REQUEST_OVERLAP);
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .creator(existedUser.getEmployee())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        return leaveRequestMapper.toDto(leaveRequest);
    }

    @Override
    @PreAuthorize("hasAuthority('UPDATE_REQUEST')")
    public LeaveRequestDTO updateLeaveRequest(UpdateLeaveRequest request, Long id) {
        validateUpdateRequest(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var existedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        LeaveRequest existedLeaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVEREQUEST_NOT_FOUND));

        if (!existedUser.getEmployee().getEmployeeId().equals(existedLeaveRequest.getCreator().getEmployeeId())) {
            throw new AppException(ErrorCode.REQUEST_NOT_BELONG_YOU);
        }

        // Kiểm tra trạng thái của LeaveRequest
        if (existedLeaveRequest.getStatus().equalsIgnoreCase("Approved") ||
                existedLeaveRequest.getStatus().equalsIgnoreCase("Approved")) {
            throw new AppException(ErrorCode.LEAVEREQUEST_CANNOT_UPDATE);
        }

        existedLeaveRequest.setTitle(request.getTitle());
        existedLeaveRequest.setStartDate(request.getStartDate());
        existedLeaveRequest.setEndDate(request.getEndDate());
        existedLeaveRequest.setReason(request.getReason());

        LeaveRequest leaveRequest = leaveRequestRepository.save(existedLeaveRequest);

        return leaveRequestMapper.toDto(leaveRequest);
    }

    @Override
    public Void deleteLeaveRequest(Long leaveRequestId) {
        // Lấy thông tin user hiện tại từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm LeaveRequest theo id
        LeaveRequest existedLeaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVEREQUEST_NOT_FOUND));

        if (!currentUser.getEmployee().getEmployeeId().equals(existedLeaveRequest.getCreator().getEmployeeId())) {
            throw new AppException(ErrorCode.REQUEST_NOT_BELONG_YOU);
        }

        // Kiểm tra trạng thái: không cho phép xóa nếu đã APPROVED hoặc REJECTED
        if (existedLeaveRequest.getStatus().equalsIgnoreCase("approved") ||
                existedLeaveRequest.getStatus().equalsIgnoreCase("rejected")) {
            throw new AppException(ErrorCode.LEAVEREQUEST_CANNOT_DELETE);
        }

        // Xóa LeaveRequest
        leaveRequestRepository.delete(existedLeaveRequest);

        // Trả về null theo kiểu Void
        return null;
    }

    @Override
    @PreAuthorize("hasAuthority('READ_EMPLOYEE_REQUESTS')")
    public Page<LeaveRequestDTO> getEmployeeRequestsAfterToday(int page, int size, FilterLeaveRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User leader = userRepository.findByUsername(currentUsername).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        Employee leaderEmployee = leader.getEmployee();

        // Lấy danh sách tất cả nhân viên trong cây phân quyền dưới quyền leader A từ database
        List<Integer> employeeIds = employeeRepository.findAllEmployeesInHierarchy(leaderEmployee.getEmployeeId());

        for (Integer employeeId : employeeIds) {

        }
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Tạo pageable cho phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        if (request.getStatus().equalsIgnoreCase("All Status")) {
            request.setStatus(null);
        }

        // Tìm tất cả LeaveRequest của các nhân viên trong cây với startDate > today
        Page<LeaveRequest> leaveRequests = leaveRequestRepository.findByCreatorEmployeeIdsAndStartDateAfterWithFilters(
                employeeIds, today, request.getFullname(), request.getStatus().toUpperCase(), pageable);

        return leaveRequests.map(leaveRequestMapper::toDto);
    }


    @Override
    @PreAuthorize("hasAuthority('PROCESS_REQUEST')")
    public LeaveRequestDTO processLeaveRequest(Long requestId, UpdateLeaveRequest request) {
        // Kiểm tra authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Lấy thông tin người dùng hiện tại
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy LeaveRequest dựa trên requestId
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.LEAVEREQUEST_NOT_FOUND));

        // Kiểm tra quyền xử lý: chỉ người có quyền PROCESS_REQUEST và là processor hoặc leader mới được xử lý
        Employee currentEmployee = currentUser.getEmployee();
        if (currentEmployee == null) {
            throw new AppException(ErrorCode.USER_NOT_EMPLOYEE);
        }

        // Kiểm tra xem currentUser có phải là processor hoặc thuộc cây phân quyền của creator
        if (!currentEmployee.getEmployeeId().equals(leaveRequest.getProcessor() != null ? leaveRequest.getProcessor().getEmployeeId() : null)) {
            // Kiểm tra xem currentUser có phải là leader của creator không
            List<Integer> subordinateIds = employeeRepository.findAllEmployeesInHierarchy(currentEmployee.getEmployeeId());
            if (!subordinateIds.contains(leaveRequest.getCreator().getEmployeeId().intValue())) {
                throw new AppException(ErrorCode.UNAUTHORIZED_PROCESS_REQUEST);
            }
        }

        // Kiểm tra trạng thái hợp lệ
        if (request.getStatus() == null ||
                (!request.getStatus().equalsIgnoreCase("APPROVED") &&
                        !request.getStatus().equalsIgnoreCase("REJECTED"))) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        if (request.getStatus().toUpperCase().equalsIgnoreCase("APPROVED")) {
            List<LeaveRequest> approvedRequests = leaveRequestRepository.findByCreatorAndStatusAndEndDateGreaterThanEqual(
                    leaveRequest.getCreator(),
                    "Approved".toUpperCase(),
                    leaveRequest.getStartDate()
            );

            if (!approvedRequests.isEmpty()) {
                throw new AppException(ErrorCode.LEAVE_REQUEST_OVERLAP);
            }
        }

        // Cập nhật trạng thái
        leaveRequest.setStatus(request.getStatus().toUpperCase());
        leaveRequest.setProcessor(currentEmployee); // Gán người xử lý hiện tại
        LeaveRequest updatedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        // Trả về DTO sau khi cập nhật
        return leaveRequestMapper.toDto(updatedLeaveRequest);
    }

    @Override
    @PreAuthorize("hasAuthority('READ_EMPLOYEE_REQUESTS')")
    public List<EmployeeDTO> getEmployeesOfLeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User leader = userRepository.findByUsername(currentUsername).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        List<Employee> employeesOfLeader = new ArrayList<>();
        Employee leaderEmployee = leader.getEmployee();

        // Lấy danh sách tất cả nhân viên trong cây phân quyền dưới quyền leader A từ database
        List<Integer> employeeIds = employeeRepository.findAllEmployeesInHierarchy(leaderEmployee.getEmployeeId());

        for (Integer employeeId : employeeIds) {
            Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                    () -> new AppException(ErrorCode.EMPLOYEE_NOT_EXSITED)
            );
            employeesOfLeader.add(employee);
        }
        return employeeMapper.toDtos(employeesOfLeader);
    }

    @Override
    public List<CalendarResponse> getCalendar(LocalDate startDate, LocalDate endDate) {
        // Kiểm tra đầu vào
        if (startDate == null || endDate == null) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        // Lấy thông tin leader hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User manager = userRepository.findByUsername(currentUsername).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        // Lấy danh sách nhân viên dưới quyền leader
        Employee leaderEmployee = manager.getEmployee();
        List<Integer> employeeIds = employeeRepository.findAllEmployeesInHierarchy(leaderEmployee.getEmployeeId());
        List<Employee> employeesOfLeader = new ArrayList<>();

        for (Integer employeeId : employeeIds) {
            Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                    () -> new AppException(ErrorCode.EMPLOYEE_NOT_EXSITED)
            );
            employeesOfLeader.add(employee);
        }

        // Khởi tạo danh sách CalendarResponse
        List<CalendarResponse> calendarResponses = new ArrayList<>();

        // Duyệt qua từng nhân viên để tạo CalendarResponse
        for (Employee employee : employeesOfLeader) {
            // Tạo CalendarResponse cho nhân viên
            CalendarResponse cr = CalendarResponse.builder()
                    .id(employee.getEmployeeId())
                    .employeeName(employee.getFullName())
                    .leaveDays(new ArrayList<>())
                    .build();

            // Tìm các LeaveRequest đã được phê duyệt trong khoảng thời gian startDate đến endDate
            List<LeaveRequest> approvedRequests = leaveRequestRepository.findApprovedLeaveRequestsByEmployeeAndDateRange(
                    employee, startDate, endDate);

            // Duyệt qua các LeaveRequest để lấy danh sách ngày nghỉ
            List<String> leaveDays = new ArrayList<>();
            for (LeaveRequest leaveRequest : approvedRequests) {
                LocalDate currentDate = leaveRequest.getStartDate();
                while (!currentDate.isAfter(leaveRequest.getEndDate())) {
                    // Chỉ thêm ngày nếu nằm trong khoảng startDate và endDate
                    if (!currentDate.isBefore(startDate) && !currentDate.isAfter(endDate)) {
                        if (!leaveDays.contains(currentDate.toString())) {
                            leaveDays.add(currentDate.toString()); // Định dạng YYYY-MM-DD
                        }
                    }
                    currentDate = currentDate.plusDays(1);
                }
            }

            cr.setLeaveDays(leaveDays);
            calendarResponses.add(cr);
        }

        return calendarResponses;
    }

    private void validateUpdateRequest(UpdateLeaveRequest request) {
        // Kiểm tra các trường bắt buộc
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
