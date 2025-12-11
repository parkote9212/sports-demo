package com.example.demo.domain.staff;

import com.example.demo.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STAFF")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Staff extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STAFF_ID")
    private Long id;

    @Column(name = "EML_ADDR", nullable = false, unique = true)
    private String email;

    @Column(name = "LGN_PWD", nullable = false)
    private String password;

    @Column(name = "STAFF_NM", nullable = false)
    private String name;

    @Column(name = "ROLE_CD", nullable = false)
    @Enumerated(EnumType.STRING) // Enum으로 관리 (ADM, MGR, INST)
    private StaffRole role;

    @Builder
    public Staff(String email, String password, String name, StaffRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}