package com.hipdev.LeaveManagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false) // Người tạo request
    @JsonManagedReference
    private Employee creator;

    @ManyToOne
    @JoinColumn(name = "processor_id") // Người xử lý request, có thể NULL
    @JsonManagedReference
    private Employee processor;

    @JoinColumn(name = "status", nullable = false)
    private String status;


}