package com.hipdev.LeaveManagement.mapper;

import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface UserMapper extends EntityMapper<UserDTO, User> {
}
