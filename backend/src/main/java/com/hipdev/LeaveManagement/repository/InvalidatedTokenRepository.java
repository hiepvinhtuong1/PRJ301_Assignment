package com.hipdev.LeaveManagement.repository;

import com.hipdev.LeaveManagement.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
