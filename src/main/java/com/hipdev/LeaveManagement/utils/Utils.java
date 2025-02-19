package com.hipdev.LeaveManagement.utils;

import com.hipdev.LeaveManagement.dto.DepartmentDTO;
import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.entity.Department;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import com.hipdev.LeaveManagement.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static DepartmentDTO mapDepartmentEntityToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    public static UserDTO mapUserEntityToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setDepartment(mapDepartmentEntityToDTO(user.getDepartment()));
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public static LeaveRequestDTO mapLeaveRequestEntityToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leaveRequest.getId());
        dto.setComment(leaveRequest.getComment());
        dto.setReason(leaveRequest.getReason());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setStatus(leaveRequest.getStatus());
        dto.setCreator(mapUserEntityToDTO(leaveRequest.getCreator()));
        dto.setProcessor(mapUserEntityToDTO(leaveRequest.getProcessor()));
        return dto;
    }

    public static UserDTO mapUserEntityToDTOPlusRequest(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setDepartment(mapDepartmentEntityToDTO(user.getDepartment()));
        userDTO.setRole(user.getRole());

        if (!user.getLeaveRequests().isEmpty()) {
            userDTO.setLeaveRequests(user.getLeaveRequests().stream().map(request->mapLeaveRequestEntityToDTO(request)).collect(Collectors.toList()));
        }
        return userDTO;
    }

    public static List<UserDTO> mapUserListEntityToListDTO(List<User> userList) {
        return userList.stream().map(user->mapUserEntityToDTO(user)).collect(Collectors.toList());
    }

    public static List<DepartmentDTO> mapDepartmentListEntityToListDTO(List<Department> departmentList) {
        return departmentList.stream().map(department->mapDepartmentEntityToDTO(department)).collect(Collectors.toList());
    }

    public static List<LeaveRequestDTO> mapRequestListEntityToListDTO(List<LeaveRequest> requestList) {
        return requestList.stream().map(request->mapLeaveRequestEntityToDTO(request)).collect(Collectors.toList());
    }
}
