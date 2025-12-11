package com.example.demo.service;

import com.example.demo.domain.member.Member;
import com.example.demo.domain.ticket.MemberTicket;
import com.example.demo.domain.ticket.TicketProduct;
import com.example.demo.domain.ticket.TicketStatus;
import com.example.demo.dto.TicketIssueRequest;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MemberTicketRepository;
import com.example.demo.repository.TicketProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketAdminService {

    private final MemberRepository memberRepository;
    private final TicketProductRepository productRepository;
    private final MemberTicketRepository memberTicketRepository;

    public void issueTicket(TicketIssueRequest request) {
        // 1. 회원 조회
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. 수동 발급용 상품 찾기 (없으면 생성)
        // 전략: "관리자 수동 발급(PT)" 라는 이름의 상품을 찾아서 연결함
        String manualProductName = "[수동발급] " + request.type().getDescription();

        TicketProduct product = productRepository.findAll().stream()
                .filter(p -> p.getName().equals(manualProductName))
                .findFirst()
                .orElseGet(() -> createManualProduct(manualProductName));

        // 3. 회원에게 이용권 지급 (입력받은 횟수와 기간 적용)
        MemberTicket ticket = MemberTicket.builder()
                .member(member)
                .product(product)
                .remainingCount(request.count())  // 관리자가 입력한 횟수
                .status(TicketStatus.ACT)         // 즉시 활성
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        memberTicketRepository.save(ticket);
    }

    // 내부 메서드: 수동용 상품이 DB에 없으면 즉석 생성
    private TicketProduct createManualProduct(String name) {
        TicketProduct newProduct = new TicketProduct(name, 0, 0);
        // 0원, 0회 (실제 지급 시 MemberTicket에서 오버라이딩하므로 껍데기만 있으면 됨)
        return productRepository.save(newProduct);
    }
}