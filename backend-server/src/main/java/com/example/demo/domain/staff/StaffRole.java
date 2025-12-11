package com.example.demo.domain.staff;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StaffRole {

    // DB에 저장되는 값(Enum 이름) : 설명
    ADM("최고관리자"),
    MGR("매니저"),
    INST("강사");

    private final String description;
}