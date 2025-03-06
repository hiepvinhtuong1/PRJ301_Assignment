package com.hipdev.LeaveManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL) // Liên kết 1-1 với User
    private User user;

    public enum Gender {
        Male, Female, Other
    }

    @ManyToOne
    @JoinColumn(name = "leader_id") // Employee có thể có 1 leader
    private Employee leader;

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL) // 1 leader quản lý nhiều employee
    private List<Employee> employeeList;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "department_id") // 1 Employee thuộc 1 Department
    private Department department;

    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL) // 1 Employee làm manager của 1 Department
    private Department managedDepartment;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL) // 1 Employee tạo nhiều LeaveRequest
    private List<LeaveRequest> createdRequests;

    @OneToMany(mappedBy = "processor", cascade = CascadeType.ALL) // 1 Leader or Manager  xử lý nhiều LeaveRequest
    private List<LeaveRequest> processedRequests;
}