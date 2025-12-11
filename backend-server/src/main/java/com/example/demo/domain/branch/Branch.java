package com.example.demo.domain.branch;

import com.example.demo.domain.common.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BRANCH")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Branch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRCH_ID")
    private Long id;

    @Column(name = "BRCH_NM", nullable = false, length = 50)
    private String name;

    @Column(name = "ADDR", nullable = false)
    private String address;

    @Column(name = "TEL_NO", nullable = false, length = 20)
    private String telephone;

    // [중요] DB는 CHAR(1)이므로 반드시 columnDefinition 명시
    @Column(name = "OPER_YN", columnDefinition = "CHAR(1)")
    @Convert(converter = BooleanToYNConverter.class)
    private boolean operationYn; // 운영 여부 (True='Y', False='N')

    @Builder
    public Branch(String name, String address, String telephone, boolean operationYn) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.operationYn = operationYn;
    }
}