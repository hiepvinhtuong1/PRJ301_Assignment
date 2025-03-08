//package com.hipdev.LeaveManagement.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Entity
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "departments")
//public class Department {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Employee> employees;
//
//    @OneToOne
//    @JoinColumn(name = "manage_id", referencedColumnName = "department_id", unique = true)
//    private Employee manager;
//}