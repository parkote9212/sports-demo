package com.example.demo.domain.schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleStatus {

    OPEN("예약중"),
    FULL("마감"),
    DONE("종료"),
    CNCL("휴강/취소");

    private final String description;
}