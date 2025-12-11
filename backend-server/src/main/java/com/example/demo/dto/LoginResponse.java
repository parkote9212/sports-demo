package com.example.demo.dto;

public record LoginResponse(
        Long id,        // 회원 or 직원 ID
        String name,    // 이름
        String role,    // "MEMBER", "ADM", "MGR", "INST"
        String token    // (데모용) 단순 식별 토큰
) {}