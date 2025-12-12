-- data.sql

-- 💡 Tech Lead Warning: 현재 비밀번호를 평문 '1234'로 하드코딩합니다.
-- Spring Security 구현 완료 후에는 반드시 BCrypt 등으로 해시된 값을 사용해야 합니다.
SET @HARDCODED_PASSWORD = '1234';

-- --------------------------------------------------------
-- 1. 기본 엔티티 (참조 대상)
-- --------------------------------------------------------

-- 1.1 회원 정보 (member)
INSERT INTO `member` (`MBR_ID`, `EML_ADDR`, `LGN_PWD`, `MBR_NM`, `MBL_NO`, `CASH_PNT`, `GAME_PNT`, `MKT_AGR_YN`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                                                         (1001, 'user1@demo.com', @HARDCODED_PASSWORD, '김모던', '010-1111-2222', 15000, 500, 'Y', NOW(), NOW()),
                                                                                                                                         (1002, 'user2@demo.com', @HARDCODED_PASSWORD, '이백엔드', '010-3333-4444', 5000, 1200, 'N', NOW(), NOW());

-- 1.2 직원 정보 (staff)
INSERT INTO `staff` (`STAFF_ID`, `EML_ADDR`, `LGN_PWD`, `STAFF_NM`, `MBL_NO`, `ROLE_CD`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                                 (2001, 'admin@demo.com', @HARDCODED_PASSWORD, '박관리', '010-9999-1111', 'ADM', NOW(), NOW()),
                                                                                                                 (2002, 'inst_a@demo.com', @HARDCODED_PASSWORD, '최강사A', '010-8888-2222', 'INST', NOW(), NOW()),
                                                                                                                 (2003, 'inst_b@demo.com', @HARDCODED_PASSWORD, '정강사B', '010-7777-3333', 'INST', NOW(), NOW());


-- 1.3 지점 정보 (branch)
INSERT INTO `branch` (`BRCH_ID`, `BRCH_NM`, `ADDR`, `TEL_NO`, `OPER_YN`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                 (3001, '강남 본점', '서울 강남구 테헤란로 123', '02-1234-5678', 'Y', NOW(), NOW()),
                                                                                                 (3002, '판교 지점', '경기 성남시 분당구 판교역로 321', '031-987-6543', 'Y', NOW(), NOW());

-- 1.4 프로그램 정보 (program)
INSERT INTO `program` (`PROG_ID`, `PROG_NM`, `EXPLN_CN`, `STD_FEE_AMT`, `DIFF_LVL_NM`, `RWD_GAME_PNT`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                                               (4001, '코어 강화 필라테스', '복부와 허리 코어를 집중적으로 강화하는 프로그램입니다.', 30000, 'INT', 10, NOW(), NOW()),
                                                                                                                               (4002, '초보자 요가 클래스', '요가 기초 자세와 호흡법을 배우는 입문 과정.', 20000, 'BEG', 5, NOW(), NOW());

-- 1.5 수강권 상품 정보 (ticket_product)
INSERT INTO `ticket_product` (`PROD_ID`, `PROD_NM`, `PROD_AMT`, `PRV_CNT`, `SALE_YN`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                              (5001, '5회 수강권', 100000, 5, 'Y', NOW(), NOW()),
                                                                                                              (5002, '10회 수강권', 180000, 10, 'Y', NOW(), NOW());

-- --------------------------------------------------------
-- 2. 중간 및 관계 엔티티
-- --------------------------------------------------------

-- 2.1 강사 상세 프로필 (staff_profile)
INSERT INTO `staff_profile` (`STAFF_ID`, `INTCN_CN`, `EXP_FLD_NM`, `REG_DT`, `UPD_DT`) VALUES
    (2002, '요가와 필라테스 전문가 최강사A입니다. 10년 경력 보유.', 'Yoga, Pilates', NOW(), NOW());

-- 2.2 지점 운영 시간 (branch_hours)
INSERT INTO `branch_hours` (`HR_ID`, `BRCH_ID`, `DOW_NM`, `OPEN_TM`, `CLS_TM`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                       (1, 3001, 'MON', '09:00:00', '21:00:00', NOW(), NOW()),
                                                                                                       (2, 3001, 'SAT', '10:00:00', '18:00:00', NOW(), NOW()),
                                                                                                       (3, 3002, 'TUE', '08:00:00', '22:00:00', NOW(), NOW());

-- 2.3 지점 소속 직원 (branch_staff)
INSERT INTO `branch_staff` (`BRCH_STF_ID`, `STAFF_ID`, `BRCH_ID`, `REG_DT`, `UPD_DT`) VALUES
                                                                                          (1, 2001, 3001, NOW(), NOW()), -- 박관리 (ADM)는 강남 본점
                                                                                          (2, 2002, 3001, NOW(), NOW()), -- 최강사A는 강남 본점
                                                                                          (3, 2003, 3002, NOW(), NOW()); -- 정강사B는 판교 지점

-- 2.4 회원 보유 수강권 (member_ticket)
-- 김모던 (1001)의 10회권 (5002) - 잔여 9회
INSERT INTO `member_ticket` (`TKT_ID`, `MBR_ID`, `PROD_ID`, `RMN_CNT`, `STTS_CD`, `STRT_DT`, `END_DT`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                                               (7001, 1001, 5002, 9, 'ACT', '2025-12-01', '2026-02-28', NOW(), NOW()),
-- 이백엔드 (1002)의 5회권 (5001) - 잔여 5회
                                                                                                                               (7002, 1002, 5001, 5, 'ACT', '2025-12-10', '2026-01-31', NOW(), NOW()),
-- 김모던의 만료된 수강권 (테스트용)
                                                                                                                               (7003, 1001, 5001, 0, 'EXH', '2024-01-01', '2024-01-31', NOW(), NOW());

-- 2.5 결제 내역 (payment)
INSERT INTO `payment` (`PAY_ID`, `ORD_NO`, `MBR_ID`, `PAY_AMT`, `STTS_CD`, `PROD_ID`, `PAY_MTHD_CD`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                                             (6001, 'ORD-20251212-0001', 1001, 180000, 'PAID', 5002, 'CARD', NOW(), NOW()), -- 김모던 10회권
                                                                                                                             (6002, 'ORD-20251212-0002', 1002, 100000, 'PAID', 5001, 'CASH', NOW(), NOW()); -- 이백엔드 5회권

-- --------------------------------------------------------
-- 3. 트랜잭션 및 로그 엔티티
-- --------------------------------------------------------

-- 3.1 수업 시간표 (schedule)
INSERT INTO `schedule` (`SCHD_ID`, `CLS_ID`, `STAFF_ID`, `STRT_DT`, `END_DT`, `MAX_NOP_CNT`, `RSV_CNT`, `STTS_CD`, `REG_DT`, `UPD_DT`) VALUES
                                                                                                                                           (8001, 4001, 2002, '2025-12-15 10:00:00', '2025-12-15 11:00:00', 15, 1, 'OPEN', NOW(), NOW()), -- 최강사A 필라테스 (1명 예약)
                                                                                                                                           (8002, 4002, 2003, '2025-12-15 19:00:00', '2025-12-15 20:00:00', 10, 0, 'OPEN', NOW(), NOW()); -- 정강사B 요가 (0명 예약)

-- 3.2 수업 예약 (reservation)
-- 김모던 (1001)이 10회권(7001)을 사용하여 8001 스케줄 예약
INSERT INTO `reservation` (`RSV_ID`, `MBR_ID`, `SCHD_ID`, `TKT_ID`, `STTS_CD`, `REG_DT`, `UPD_DT`) VALUES
    (9001, 1001, 8001, 7001, 'RSV', NOW(), NOW());

-- 3.3 수강권 변동 이력 (ticket_history)
INSERT INTO `ticket_history` (`HIST_ID`, `TKT_ID`, `CHG_TYPE_CD`, `CHG_CNT`, `POCS_STF_ID`, `OCCR_DT`) VALUES
                                                                                                           (1, 7001, 'BUY', 10, 2001, NOW()), -- 김모던 10회권 구매
                                                                                                           (2, 7001, 'USE', -1, NULL, NOW()), -- 김모던 1회 사용 (예약 9001에 따른 차감)
                                                                                                           (3, 7002, 'BUY', 5, 2001, NOW()); -- 이백엔드 5회권 구매

-- 3.4 이용권 거래(양도) 게시글 (market_post)
-- 김모던 (1001)이 자신의 10회권 (7001)을 양도글 등록 (상태: SALE)
INSERT INTO `market_post` (`POST_ID`, `MBR_ID`, `TKT_ID`, `SALE_AMT`, `STTS_CD`, `REG_DT`, `UPD_DT`) VALUES
    (10001, 1001, 7001, 150000, 'SALE', NOW(), NOW());

-- --------------------------------------------------------
-- 초기 데이터 로딩 완료.
-- --------------------------------------------------------