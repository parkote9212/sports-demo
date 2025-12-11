package com.example.demo.service;

import com.example.demo.domain.member.Member;
import com.example.demo.domain.staff.Staff;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final StaffRepository staffRepository;

    public LoginResponse login(LoginRequest request) {
        // 1. 회원(Member) 테이블 조회 시도
        Optional<Member> memberOpt = memberRepository.findByEmail(request.email());
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            if (!member.getPassword().equals(request.password())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            return new LoginResponse(member.getId(), member.getName(), "MEMBER", "TOKEN-MEMBER-" + member.getId());
        }

        // 2. 없으면 직원(Staff) 테이블 조회 시도
        Staff staff = staffRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!staff.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponse(
                staff.getId(),
                staff.getName(),
                staff.getRole().name(), // ADM, MGR, INST
                "TOKEN-STAFF-" + staff.getId()
        );
    }
}