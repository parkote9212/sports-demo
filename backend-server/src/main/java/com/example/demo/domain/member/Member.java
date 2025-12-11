package com.example.demo.domain.member;

import com.example.demo.domain.common.BaseTimeEntity;
import com.example.demo.domain.common.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MBR_ID")
    private Long id;

    @Column(name = "EML_ADDR", nullable = false, unique = true)
    private String email;

    @Column(name = "LGN_PWD", nullable = false)
    private String password;

    @Column(name = "MBR_NM", nullable = false)
    private String name;

    @Column(name = "MBL_NO", nullable = false)
    private String mobile;

    @Column(name = "CASH_PNT")
    private int cashPoint;

    @Column(name = "MKT_AGR_YN", columnDefinition = "CHAR(1)")
    @Convert(converter = BooleanToYNConverter.class)
    private boolean marketingAgree;

    @ColumnDefault("0")
    @Column(name = "GAME_PNT")
    private Integer gamePnt;

    @Builder
    public Member(String email, String password, String name, String mobile, boolean marketingAgree) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.mobile = mobile;
        this.marketingAgree = marketingAgree;
        this.cashPoint = 0;
    }

    // 비즈니스 로직: 포인트 사용
    public void usePoint(int amount) {
        if (this.cashPoint < amount) throw new IllegalStateException("포인트 부족");
        this.cashPoint -= amount;
    }
}
