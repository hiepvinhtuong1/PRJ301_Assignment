package com.hipdev.LeaveManagement.repository;

import com.hipdev.LeaveManagement.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    @Query("SELECT lr FROM LeaveRequest lr " +
            "WHERE (:status IS NULL OR lr.status = :status) " +
            "AND (:startDate IS NULL OR lr.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR lr.endDate <= :endDate) " +
            "AND (:creatorId IS NULL OR lr.creator.employeeId = :creatorId) " +
            "AND (:processorId IS NULL OR lr.processor.employeeId = :processorId)")
    Page<LeaveRequest> findByFilters(
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("creatorId") Integer creatorId,
            @Param("processorId") Integer processorId,
            Pageable pageable);
}
