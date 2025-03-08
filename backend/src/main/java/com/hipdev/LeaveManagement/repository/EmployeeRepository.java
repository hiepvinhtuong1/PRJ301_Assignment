package com.hipdev.LeaveManagement.repository;

import com.hipdev.LeaveManagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Lấy tất cả nhân viên trong cây phân quyền (dùng truy vấn đệ quy SQL)
    @Query(value = "WITH employee_hierarchy AS (\n" +
            "    SELECT employee_id, full_name, leader_id\n" +
            "    FROM dbo.employees eh\n" +
            "    WHERE leader_id = :leaderId  -- Không cần để trong dấu nháy đơn nếu leader_id là kiểu số\n" +
            "    UNION ALL\n" +
            "    SELECT e.employee_id, e.full_name, e.leader_id\n" +
            "    FROM dbo.employees e\n" +
            "    INNER JOIN employee_hierarchy eh ON e.leader_id = eh.employee_id\n" +
            ")\n" +
            "SELECT employee_id FROM employee_hierarchy", nativeQuery = true)
    List<Integer> findAllEmployeesInHierarchy(Integer leaderId);
}
