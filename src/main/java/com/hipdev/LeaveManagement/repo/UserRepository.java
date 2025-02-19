package com.hipdev.LeaveManagement.repo;

import com.hipdev.LeaveManagement.entity.Department;
import com.hipdev.LeaveManagement.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    Optional<List<User>> findUsersByLeader(User leader);


    boolean existsByUsername(@NotBlank(message = "Username is required") String username);
}
