package com.hipdev.LeaveManagement.mapper;

import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {
}
