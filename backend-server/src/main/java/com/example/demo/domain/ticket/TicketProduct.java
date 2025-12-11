package com.example.demo.domain.ticket;

import com.example.demo.domain.common.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TICKET_PRODUCT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROD_ID")
    private Long id;

    @Column(name = "PROD_NM", nullable = false)
    private String name;

    @Column(name = "PROD_AMT", nullable = false)
    private int price;

    @Column(name = "PRV_CNT", nullable = false)
    private int providedCount; // 제공 횟수

    @Column(name = "SALE_YN", columnDefinition = "CHAR(1)")
    @Convert(converter = BooleanToYNConverter.class)
    private boolean saleYn;

    public TicketProduct(String name, int price, int providedCount) {
        this.name = name;
        this.price = price;
        this.providedCount = providedCount;
        this.saleYn = false; // 수동 상품은 상점에 노출 안 됨
    }
}