package com.hipdev.LeaveManagement.service.interfac;

import com.hipdev.LeaveManagement.dto.Response;

public interface IDepartmentService {

    Response getAllDepartments();

    Response getDepartmentById(Long id);
}
