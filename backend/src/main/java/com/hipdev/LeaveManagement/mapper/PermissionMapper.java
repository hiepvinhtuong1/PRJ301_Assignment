package com.hipdev.LeaveManagement.mapper;

import com.hipdev.LeaveManagement.dto.PermissionDTO;
import com.hipdev.LeaveManagement.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
}
