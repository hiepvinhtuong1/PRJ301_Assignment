package com.hipdev.LeaveManagement.dto;

import lombok.Data;
import org.mapstruct.Mapper;

import java.util.List;

@Data
@Mapper
public class RoleDTO {
    private String roleName;
    private List<PermissionDTO> permissions;
}
