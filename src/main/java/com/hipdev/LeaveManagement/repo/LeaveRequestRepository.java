package com.hipdev.LeaveManagement.repo;

import com.hipdev.LeaveManagement.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByStartDateAfterAndEndDateBeforeAndStatus(LocalDate startDate, LocalDate endDate, String status);
}
