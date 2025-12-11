package com.example.demo.domain.schedule;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "PROGRAM")
@Getter
public class Program {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROG_ID")
    private Long id;

    @Column(name = "PROG_NM")
    private String name; // 요가, PT 등

    // 사용 여부 (USE_YN)
    @Column(name = "USE_YN", columnDefinition = "CHAR(1)")
    @Convert(converter = com.example.demo.domain.common.BooleanToYNConverter.class)
    private boolean useYn;

    @Lob
    @Column(name = "EXPLN_CN")
    private String explnCn;

    @NotNull
    @Column(name = "STD_FEE_AMT", nullable = false)
    private Integer stdFeeAmt;

    @Size(max = 20)
    @Column(name = "DIFF_LVL_NM", length = 20)
    private String diffLvlNm;

    @ColumnDefault("0")
    @Column(name = "RWD_GAME_PNT")
    private Integer rwdGamePnt;
}
