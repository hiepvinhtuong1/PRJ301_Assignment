package com.hipdev.LeaveManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Reason is requried")
    private String reason;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date must be in the future")
    private LocalDate endDate;

    private String status;

    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "processor_id")
    private User processor;
}