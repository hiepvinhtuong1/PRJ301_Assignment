package com.hipdev.LeaveManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is requried")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "FullName is required")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private User leader;

    @OneToMany(mappedBy = "creator"
            , cascade = CascadeType.ALL, orphanRemoval = true
            , fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveRequests = new ArrayList<>();

    @OneToMany(mappedBy = "processor"
            , cascade = CascadeType.ALL, orphanRemoval = true
            , fetch = FetchType.LAZY)
    private List<LeaveRequest> processRequests = new ArrayList<>();

    @ManyToMany
    Set<Role> roles = new HashSet<>();

}
