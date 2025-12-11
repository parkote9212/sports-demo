package com.example.demo.domain.ticket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProgramType {
    PT("헬스 PT"),
    GX("GX 그룹수업"),
    PILA1("필라테스 1:1"),
    PILA6("필라테스 6:1");

    private final String description;
}