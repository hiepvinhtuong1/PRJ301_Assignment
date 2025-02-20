package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.DepartmentDTO;
import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.Department;
import com.hipdev.LeaveManagement.exception.MyException;
import com.hipdev.LeaveManagement.repo.DepartmentRepository;
import com.hipdev.LeaveManagement.service.interfac.IDepartmentService;
import com.hipdev.LeaveManagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public Response getAllDepartments() {
        Response response = new Response();
        try {
            List<Department> departmentList = departmentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<DepartmentDTO> departmentDTOList = Utils.mapDepartmentListEntityToListDTO(departmentList);
            response.setMessage("success");
            response.setStatusCode(200);
            response.setDepartments(departmentDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error in getAllDepartments");
        }
        return response;
    }

    @Override
    public Response getDepartmentById(Long id) {
        Response response = new Response();
        try {
            Department department = departmentRepository.findById(id).orElseThrow(() -> new MyException("Error in getDepartmentById"));
            DepartmentDTO departmentDTO = Utils.mapDepartmentEntityToDTO(department);
            response.setMessage("success");
            response.setStatusCode(200);
            response.setDepartment(departmentDTO);
        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error in getAllDepartments");

        }
        return response;
    }
}
