package com.hipdev.LeaveManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true) // Liên kết 1-1 với Employee
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "leader_id") // User có thể có 1 leader
    private User leader;

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL) // 1 leader quản lý nhiều user
    private List<User> subordinates;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false) // 1 User thuộc 1 Department
    private Department department;

    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL) // 1 User làm manager của 1 Department
    private Department managedDepartment;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    ) // 1 User có nhiều Role
    private List<Role> roles;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL) // 1 User tạo nhiều LeaveRequest
    private List<LeaveRequest> createdRequests;

    @OneToMany(mappedBy = "processor", cascade = CascadeType.ALL) // 1 User xử lý nhiều LeaveRequest
    private List<LeaveRequest> processedRequests;
}