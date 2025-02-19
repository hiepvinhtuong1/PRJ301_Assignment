package com.hipdev.LeaveManagement.repo;

import com.hipdev.LeaveManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<List<User>> findUsersByLeaveId(Long leaveId);

    Optional<List<User>> findUsersByDepartmentId(Long departmentId);
}
