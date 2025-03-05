package com.hipdev.LeaveManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false) // Người tạo request
    private Employee creator;

    @ManyToOne
    @JoinColumn(name = "processor_id") // Người xử lý request, có thể NULL
    private Employee processor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.Pending;

    public enum Status {
        Pending, Approved, Rejected
    }
}