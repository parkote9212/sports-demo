package com.example.demo.repository;

import com.example.demo.domain.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    // 운영 중인 지점만 조회 (드롭박스 등에 사용)
    // select * from branch where oper_yn = 'Y'
    List<Branch> findAllByOperationYnTrue();
}