package com.hipdev.LeaveManagement.utils;

import com.hipdev.LeaveManagement.dto.DepartmentDTO;
import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.entity.Department;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static DepartmentDTO mapDepartmentEntityToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        if (department != null) {
            dto.setId(department.getId());
            if (department.getName() != null) {
                dto.setName(department.getName());
            }
        }
        return dto;
    }

    public static UserDTO mapUserEntityToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        if (user != null) {
            userDTO.setId(user.getId());
            if (user.getUsername() != null) {
                userDTO.setUsername(user.getUsername());
            }
            if (user.getFullName() != null) {
                userDTO.setFullName(user.getFullName());
            }
            if (user.getDepartment() != null) {
                userDTO.setDepartment(mapDepartmentEntityToDTO(user.getDepartment()));
            }
            if (user.getRole() != null) {
                userDTO.setRole(user.getRole());
            }
        }
        return userDTO;
    }

    public static LeaveRequestDTO mapLeaveRequestEntityToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        if (leaveRequest != null) {
            dto.setId(leaveRequest.getId());
            if (leaveRequest.getComment() != null) {
                dto.setComment(leaveRequest.getComment());
            }
            if (leaveRequest.getReason() != null) {
                dto.setReason(leaveRequest.getReason());
            }
            if (leaveRequest.getStartDate() != null) {
                dto.setStartDate(leaveRequest.getStartDate());
            }
            if (leaveRequest.getEndDate() != null) {
                dto.setEndDate(leaveRequest.getEndDate());
            }
            if (leaveRequest.getStatus() != null) {
                dto.setStatus(leaveRequest.getStatus());
            }
            if (leaveRequest.getCreator() != null) {
                dto.setCreator(mapUserEntityToDTO(leaveRequest.getCreator()));
            }
            if (leaveRequest.getProcessor() != null) {
                dto.setProcessor(mapUserEntityToDTO(leaveRequest.getProcessor()));
            }
        }
        return dto;
    }

    public static UserDTO mapUserEntityToDTOPlusRequest(User user) {
        UserDTO userDTO = new UserDTO();
        if (user != null) {
            userDTO.setId(user.getId());
            if (user.getUsername() != null) {
                userDTO.setUsername(user.getUsername());
            }
            if (user.getFullName() != null) {
                userDTO.setFullName(user.getFullName());
            }
            if (user.getDepartment() != null) {
                userDTO.setDepartment(mapDepartmentEntityToDTO(user.getDepartment()));
            }
            if (user.getRole() != null) {
                userDTO.setRole(user.getRole());
            }

            if (user.getLeaveRequests() != null && !user.getLeaveRequests().isEmpty()) {
                userDTO.setLeaveRequests(user.getLeaveRequests().stream()
                        .map(request -> mapLeaveRequestEntityToDTO(request))
                        .collect(Collectors.toList()));
            }
        }
        return userDTO;
    }

    public static List<UserDTO> mapUserListEntityToListDTO(List<User> userList) {
        return userList != null ? userList.stream()
                .map(user -> mapUserEntityToDTO(user))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    public static List<DepartmentDTO> mapDepartmentListEntityToListDTO(List<Department> departmentList) {
        return departmentList != null ? departmentList.stream()
                .map(department -> mapDepartmentEntityToDTO(department))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    public static List<LeaveRequestDTO> mapRequestListEntityToListDTO(List<LeaveRequest> requestList) {
        return requestList != null ? requestList.stream()
                .map(request -> mapLeaveRequestEntityToDTO(request))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

}
