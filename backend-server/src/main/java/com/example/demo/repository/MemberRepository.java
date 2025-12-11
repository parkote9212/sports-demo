package com.example.demo.repository;

import com.example.demo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 로그인용: 이메일로 회원 찾기
    Optional<Member> findByEmail(String email);
}