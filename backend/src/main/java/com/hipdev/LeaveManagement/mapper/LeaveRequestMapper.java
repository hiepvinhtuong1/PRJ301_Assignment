package com.hipdev.LeaveManagement.mapper;

import com.hipdev.LeaveManagement.dto.LeaveRequestDTO;
import com.hipdev.LeaveManagement.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface LeaveRequestMapper extends EntityMapper<LeaveRequestDTO, LeaveRequest> {

}
