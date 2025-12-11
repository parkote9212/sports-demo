package com.example.demo.mapper;

import com.example.demo.dto.ScheduleAdminDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
    /**
     * 날짜별 스케줄 조회
     * @param date 조회할 날짜 (YYYY-MM-DD)
     * @param branchId 지점 ID (Null이면 전체 조회 - 관리자용)
     */
    List<ScheduleAdminDto> selectDailySchedules(
            @Param("date") String date,
            @Param("branchId") Long branchId
    );
}