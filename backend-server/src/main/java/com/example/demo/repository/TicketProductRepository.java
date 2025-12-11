package com.example.demo.repository;

import com.example.demo.domain.ticket.TicketProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketProductRepository extends JpaRepository<TicketProduct, Long> {
    List<TicketProduct> findBySaleYn(boolean saleYn);
}