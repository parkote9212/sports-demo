package com.example.demo.repository;

import com.example.demo.domain.staff.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    // AuthService에서 사용하는 메서드
    Optional<Staff> findByEmail(String email);
}