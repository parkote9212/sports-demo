-- --------------------------------------------------------
-- 💡 Tech Lead Guide: 안정적인 스키마 생성을 위한 환경 설정 및 외부 키 체크 비활성화
-- --------------------------------------------------------
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 모든 기존 테이블 DROP (순서 관계 없이 안전하게 삭제)
DROP TABLE IF EXISTS `branch_hours`;
DROP TABLE IF EXISTS `branch_staff`;
DROP TABLE IF EXISTS `market_post`;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `staff_profile`;
DROP TABLE IF EXISTS `ticket_history`;
DROP TABLE IF EXISTS `member_ticket`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `program`;
DROP TABLE IF EXISTS `branch`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `staff`;
DROP TABLE IF EXISTS `ticket_product`;

-- --------------------------------------------------------
-- 1. 기본 엔티티 (참조 대상)
-- --------------------------------------------------------

-- 테이블 demo_db.member 구조 내보내기 (수정 일시 추가)
CREATE TABLE IF NOT EXISTS `member` (
                                        `MBR_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '회원 고유 식별자',
                                        `EML_ADDR` varchar(100) NOT NULL COMMENT '이메일 주소 (로그인 ID)',
                                        `LGN_PWD` varchar(255) NOT NULL COMMENT '로그인 비밀번호 (암호화)',
                                        `MBR_NM` varchar(50) NOT NULL COMMENT '회원 성명',
                                        `MBL_NO` varchar(20) NOT NULL COMMENT '휴대전화번호',
                                        `CASH_PNT` int(11) DEFAULT 0 COMMENT '캐시 포인트(결제용)',
                                        `GAME_PNT` int(11) DEFAULT 0 COMMENT '게이미피케이션용 포인트',
                                        `MKT_AGR_YN` char(1) DEFAULT 'N' COMMENT '마케팅 동의 여부 (Y/N)',
                                        `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                        `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                        PRIMARY KEY (`MBR_ID`),
                                        UNIQUE KEY `EML_ADDR` (`EML_ADDR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 정보';

-- 테이블 demo_db.staff 구조 내보내기 (수정 일시 추가)
CREATE TABLE IF NOT EXISTS `staff` (
                                       `STAFF_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '직원 고유 식별자',
                                       `EML_ADDR` varchar(100) NOT NULL COMMENT '이메일 주소',
                                       `LGN_PWD` varchar(255) NOT NULL COMMENT '로그인 비밀번호',
                                       `STAFF_NM` varchar(50) NOT NULL COMMENT '직원 성명',
                                       `MBL_NO` varchar(20) NOT NULL COMMENT '휴대전화번호',
                                       `ROLE_CD` varchar(20) NOT NULL COMMENT '역할 코드 (ADM, MGR, INST)',
                                       `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                       `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                       PRIMARY KEY (`STAFF_ID`),
                                       UNIQUE KEY `EML_ADDR` (`EML_ADDR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='직원 정보';

-- 테이블 demo_db.branch 구조 내보내기 (수정 일시 추가)
CREATE TABLE IF NOT EXISTS `branch` (
                                        `BRCH_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '지점 고유 식별자',
                                        `BRCH_NM` varchar(50) NOT NULL COMMENT '지점명',
                                        `ADDR` varchar(255) NOT NULL COMMENT '주소',
                                        `TEL_NO` varchar(20) NOT NULL COMMENT '전화번호',
                                        `OPER_YN` char(1) DEFAULT 'Y' COMMENT '운영 여부',
                                        `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                        `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                        PRIMARY KEY (`BRCH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='지점 정보';

-- 테이블 demo_db.program 구조 내보내기 (수정 일시 추가)
CREATE TABLE IF NOT EXISTS `program` (
                                         `PROG_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '프로그램 식별자',
                                         `PROG_NM` varchar(100) NOT NULL COMMENT '프로그램명',
                                         `EXPLN_CN` text DEFAULT NULL COMMENT '설명 내용',
                                         `STD_FEE_AMT` int(11) NOT NULL COMMENT '기준 수강료',
                                         `DIFF_LVL_NM` varchar(20) DEFAULT NULL COMMENT '난이도 (BEG, INT, ADV)',
                                         `USE_YN` char(1) DEFAULT 'Y' COMMENT '사용 여부',
                                         `RWD_GAME_PNT` int(11) DEFAULT 0 COMMENT '보상 게임 포인트',
                                         `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                         `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                         PRIMARY KEY (`PROG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='프로그램 정보';

-- 테이블 demo_db.ticket_product 구조 내보내기 (수정 일시 추가)
CREATE TABLE IF NOT EXISTS `ticket_product` (
                                                `PROD_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '상품 식별자',
                                                `PROD_NM` varchar(100) NOT NULL COMMENT '상품명',
                                                `PROD_AMT` int(11) NOT NULL COMMENT '상품 금액',
                                                `PRV_CNT` int(11) NOT NULL COMMENT '제공 횟수',
                                                `SALE_YN` char(1) DEFAULT 'Y' COMMENT '판매 여부',
                                                `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                                `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                                PRIMARY KEY (`PROD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='수강권 상품 정보';


-- --------------------------------------------------------
-- 2. 중간 및 관계 엔티티 (FK 참조 시작)
-- --------------------------------------------------------

-- 테이블 demo_db.staff_profile 구조 내보내기
CREATE TABLE IF NOT EXISTS `staff_profile` (
                                               `STAFF_ID` bigint(20) NOT NULL COMMENT '직원 식별자 (FK)',
                                               `INTCN_CN` text DEFAULT NULL COMMENT '소개 내용',
                                               `EXP_FLD_NM` varchar(100) DEFAULT NULL COMMENT '전문 분야명',
                                               `PRFL_FILE_ID` bigint(20) DEFAULT NULL COMMENT '프로필 사진 파일 ID',
                                               `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                               `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                               PRIMARY KEY (`STAFF_ID`),
                                               CONSTRAINT `FK_STAFF_PROFILE_STAFF` FOREIGN KEY (`STAFF_ID`) REFERENCES `staff` (`STAFF_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='강사 상세 프로필';

-- 테이블 demo_db.branch_hours 구조 내보내기
CREATE TABLE IF NOT EXISTS `branch_hours` (
                                              `HR_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '시간 식별자',
                                              `BRCH_ID` bigint(20) NOT NULL COMMENT '지점 식별자',
                                              `DOW_NM` varchar(15) NOT NULL COMMENT '요일명 (MON, TUE...)',
                                              `OPEN_TM` time NOT NULL COMMENT '개점 시간',
                                              `CLS_TM` time NOT NULL COMMENT '폐점 시간',
                                              `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                              `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                              PRIMARY KEY (`HR_ID`),
                                              KEY `FK_BH_BRANCH` (`BRCH_ID`),
                                              CONSTRAINT `FK_BH_BRANCH` FOREIGN KEY (`BRCH_ID`) REFERENCES `branch` (`BRCH_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='지점 운영 시간';

-- 테이블 demo_db.branch_staff 구조 내보내기
CREATE TABLE IF NOT EXISTS `branch_staff` (
                                              `BRCH_STF_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '지점 직원 매핑 ID',
                                              `STAFF_ID` bigint(20) NOT NULL COMMENT '직원 식별자',
                                              `BRCH_ID` bigint(20) NOT NULL COMMENT '지점 식별자',
                                              `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                              `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                              PRIMARY KEY (`BRCH_STF_ID`),
                                              KEY `FK_BS_STAFF` (`STAFF_ID`),
                                              KEY `FK_BS_BRANCH` (`BRCH_ID`),
                                              CONSTRAINT `FK_BS_BRANCH` FOREIGN KEY (`BRCH_ID`) REFERENCES `branch` (`BRCH_ID`),
                                              CONSTRAINT `FK_BS_STAFF` FOREIGN KEY (`STAFF_ID`) REFERENCES `staff` (`STAFF_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='지점 소속 직원';

-- 테이블 demo_db.member_ticket 구조 내보내기
CREATE TABLE IF NOT EXISTS `member_ticket` (
                                               `TKT_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '수강권 식별자',
                                               `MBR_ID` bigint(20) NOT NULL COMMENT '회원 식별자',
                                               `PROD_ID` bigint(20) NOT NULL COMMENT '상품 식별자',
                                               `RMN_CNT` int(11) NOT NULL COMMENT '잔여 횟수',
                                               `STTS_CD` varchar(20) NOT NULL COMMENT '상태 코드 (ACT, EXP, EXH, STP)',
                                               `STRT_DT` date DEFAULT NULL COMMENT '유효 시작일',
                                               `END_DT` date DEFAULT NULL COMMENT '유효 종료일',
                                               `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                               `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                               PRIMARY KEY (`TKT_ID`),
                                               KEY `FK_MT_MEMBER` (`MBR_ID`),
                                               KEY `FK_MT_PRODUCT` (`PROD_ID`),
                                               CONSTRAINT `FK_MT_MEMBER` FOREIGN KEY (`MBR_ID`) REFERENCES `member` (`MBR_ID`),
                                               CONSTRAINT `FK_MT_PRODUCT` FOREIGN KEY (`PROD_ID`) REFERENCES `ticket_product` (`PROD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 보유 수강권';

-- 테이블 demo_db.payment 구조 내보내기
CREATE TABLE IF NOT EXISTS `payment` (
                                         `PAY_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '결제 식별자',
                                         `ORD_NO` varchar(100) NOT NULL COMMENT '주문 번호',
                                         `MBR_ID` bigint(20) NOT NULL COMMENT '회원 식별자',
                                         `PAY_AMT` int(11) NOT NULL COMMENT '결제 금액',
                                         `STTS_CD` varchar(20) NOT NULL COMMENT '상태 코드 (PAID, WAIT, CNCL, FAIL)',
                                         `REG_DT` datetime DEFAULT current_timestamp() COMMENT '결제 일시',
                                         `PROD_ID` bigint(20) DEFAULT NULL COMMENT '상품 식별자',
                                         `PAY_MTHD_CD` varchar(20) DEFAULT NULL COMMENT '결제 수단 코드',
                                         `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                         PRIMARY KEY (`PAY_ID`),
                                         UNIQUE KEY `ORD_NO` (`ORD_NO`),
                                         KEY `FK_PAY_MEMBER` (`MBR_ID`),
                                         KEY `FK_PAY_PRODUCT` (`PROD_ID`),
                                         CONSTRAINT `FK_PAY_MEMBER` FOREIGN KEY (`MBR_ID`) REFERENCES `member` (`MBR_ID`),
                                         CONSTRAINT `FK_PAY_PRODUCT` FOREIGN KEY (`PROD_ID`) REFERENCES `ticket_product` (`PROD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='결제 내역';


-- --------------------------------------------------------
-- 3. 트랜잭션 및 로그 엔티티
-- --------------------------------------------------------

-- 테이블 demo_db.schedule 구조 내보내기
CREATE TABLE IF NOT EXISTS `schedule` (
                                          `SCHD_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '스케줄 식별자',
                                          `CLS_ID` bigint(20) NOT NULL COMMENT '강좌(프로그램) 식별자',
                                          `STAFF_ID` bigint(20) NOT NULL COMMENT '담당 강사 식별자',
                                          `STRT_DT` datetime NOT NULL COMMENT '시작 일시',
                                          `END_DT` datetime NOT NULL COMMENT '종료 일시',
                                          `MAX_NOP_CNT` int(11) NOT NULL COMMENT '최대 인원 수',
                                          `RSV_CNT` int(11) DEFAULT 0 COMMENT '현재 예약 인원',
                                          `STTS_CD` varchar(20) NOT NULL COMMENT '상태 코드 (OPEN, FULL, DONE, CNCL)',
                                          `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                          `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                          PRIMARY KEY (`SCHD_ID`),
                                          KEY `FK_SCHD_PROG` (`CLS_ID`),
                                          KEY `FK_SCHD_STAFF` (`STAFF_ID`),
                                          CONSTRAINT `FK_SCHD_PROG` FOREIGN KEY (`CLS_ID`) REFERENCES `program` (`PROG_ID`),
                                          CONSTRAINT `FK_SCHD_STAFF` FOREIGN KEY (`STAFF_ID`) REFERENCES `staff` (`STAFF_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='수업 시간표';

-- 테이블 demo_db.reservation 구조 내보내기
CREATE TABLE IF NOT EXISTS `reservation` (
                                             `RSV_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '예약 식별자',
                                             `MBR_ID` bigint(20) NOT NULL COMMENT '회원 식별자',
                                             `SCHD_ID` bigint(20) NOT NULL COMMENT '스케줄 식별자',
                                             `TKT_ID` bigint(20) DEFAULT NULL COMMENT '사용 수강권 식별자',
                                             `STTS_CD` varchar(20) NOT NULL COMMENT '상태 코드 (RSV, ATD, NOS, CNCL)',
                                             `REG_DT` datetime DEFAULT current_timestamp() COMMENT '예약 일시',
                                             `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                             PRIMARY KEY (`RSV_ID`),
                                             KEY `FK_RSV_MEMBER` (`MBR_ID`),
                                             KEY `FK_RSV_SCHEDULE` (`SCHD_ID`),
                                             KEY `FK_RSV_TICKET` (`TKT_ID`),
                                             CONSTRAINT `FK_RSV_MEMBER` FOREIGN KEY (`MBR_ID`) REFERENCES `member` (`MBR_ID`),
                                             CONSTRAINT `FK_RSV_SCHEDULE` FOREIGN KEY (`SCHD_ID`) REFERENCES `schedule` (`SCHD_ID`),
                                             CONSTRAINT `FK_RSV_TICKET` FOREIGN KEY (`TKT_ID`) REFERENCES `member_ticket` (`TKT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='수업 예약';

-- 테이블 demo_db.ticket_history 구조 내보내기
CREATE TABLE IF NOT EXISTS `ticket_history` (
                                                `HIST_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '이력 식별자',
                                                `TKT_ID` bigint(20) NOT NULL COMMENT '수강권 식별자',
                                                `CHG_TYPE_CD` varchar(30) NOT NULL COMMENT '변경 유형 (USE, BUY, CNCL, ADJ)',
                                                `CHG_CNT` int(11) NOT NULL COMMENT '변경 수량',
                                                `POCS_STF_ID` bigint(20) DEFAULT NULL COMMENT '처리 직원 식별자',
                                                `OCCR_DT` datetime DEFAULT current_timestamp() COMMENT '발생 일시',
                                                PRIMARY KEY (`HIST_ID`),
                                                KEY `FK_TH_TICKET` (`TKT_ID`),
                                                CONSTRAINT `FK_TH_TICKET` FOREIGN KEY (`TKT_ID`) REFERENCES `member_ticket` (`TKT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='수강권 변동 이력';

-- 테이블 demo_db.market_post 구조 내보내기
CREATE TABLE IF NOT EXISTS `market_post` (
                                             `POST_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '게시글 식별자',
                                             `MBR_ID` bigint(20) NOT NULL COMMENT '판매 회원 식별자',
                                             `TKT_ID` bigint(20) NOT NULL COMMENT '판매 수강권 식별자',
                                             `SALE_AMT` int(11) NOT NULL COMMENT '판매 금액',
                                             `STTS_CD` varchar(20) NOT NULL COMMENT '상태 코드 (SALE, SOLD, HIDE)',
                                             `REG_DT` datetime DEFAULT current_timestamp() COMMENT '등록 일시',
                                             `UPD_DT` datetime DEFAULT NULL ON UPDATE current_timestamp() COMMENT '최종 수정 일시',
                                             PRIMARY KEY (`POST_ID`),
                                             KEY `FK_MKT_MEMBER` (`MBR_ID`),
                                             KEY `FK_MKT_TICKET` (`TKT_ID`),
                                             CONSTRAINT `FK_MKT_MEMBER` FOREIGN KEY (`MBR_ID`) REFERENCES `member` (`MBR_ID`),
                                             CONSTRAINT `FK_MKT_TICKET` FOREIGN KEY (`TKT_ID`) REFERENCES `member_ticket` (`TKT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='이용권 거래(양도) 게시글';


-- --------------------------------------------------------
-- 💡 Tech Lead Guide: 외부 키 체크 다시 활성화
-- --------------------------------------------------------
/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;