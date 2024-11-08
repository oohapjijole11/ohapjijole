package com.sparta.final_project.domain.auction.entity;

public enum Status {
    WAITING, //
    BID, // 경매가 시작 대기중인 상태
    IN_PROGRESS, // 진행중인 상태
    SUCCESSBID, // 경매가 성공적으로 종료된 상태
    FAILBID // 경매가 실패하거나 취소된 상태
}
