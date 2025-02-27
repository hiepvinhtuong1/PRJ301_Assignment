package com.hipdev.LeaveManagement.mapper;

import com.hipdev.LeaveManagement.dto.RoleDTO;
import com.hipdev.LeaveManagement.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
}
