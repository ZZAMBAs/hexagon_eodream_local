package com.example.contractservice.deposit.common;

/**
 * PENDING: 계약 결제 완료 이후 관리자 예치금에 반영되기 이전
 * COMPLETED: 관리자 예치금에 반영
 * CANCELLED: 관리자 예치금 반영 전, 계약 취소
 * FAILED: 관리자 예치금 반영 처리 실패
 */
public enum DepositPendingStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED
}
