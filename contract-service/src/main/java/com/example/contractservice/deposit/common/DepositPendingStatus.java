package com.example.contractservice.deposit.common;

/**
 * PENDING: 계약 결제가 완료되어 관리자 예치금에 반영되기 이전
 * COMPLETED: 관리자 예치금에 반영된 건
 * CANCELLED: 취소된 계약
 */
public enum DepositPendingStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}
