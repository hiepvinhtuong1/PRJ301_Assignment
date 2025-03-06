package com.hipdev.LeaveManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    private String permissionName; // name làm ID

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles; // 1 Permission có thể thuộc nhiều Role
}