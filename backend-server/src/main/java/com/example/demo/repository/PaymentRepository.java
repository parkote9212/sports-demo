package com.example.demo.repository;

import com.example.demo.domain.payment.Payment;
import com.example.demo.domain.payment.PaymentMethod;
import com.example.demo.domain.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByMethodAndStatus(PaymentMethod method, PaymentStatus status);
    
    @Query("SELECT p FROM Payment p JOIN FETCH p.member JOIN FETCH p.product WHERE p.method = :method AND p.status = :status")
    List<Payment> findByMethodAndStatusWithDetails(PaymentMethod method, PaymentStatus status);
}
