package com.hipdev.LeaveManagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Role {
    @Id
    private String name;
    private String description;

    @ManyToMany
    private Set<Permission> permissions = new HashSet<>();
}

