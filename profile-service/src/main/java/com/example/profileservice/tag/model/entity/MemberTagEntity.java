package com.example.profileservice.tag.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members_tags",
        uniqueConstraints = {
                // 한 회원이 같은 태그를 중복해서 등록할 수 없도록 복합 유니크 제약 조건 추가
                @UniqueConstraint(
                        name = "UK_member_tag_unique",
                        columnNames = {"member_code", "tag_code"}
                )
        },
        indexes = {
                // memberCode 기준으로 태그를 빠르게 조회하기 위한 인덱스
                @Index(name = "IDX_member_code", columnList = "member_code"),
                // tagCode 기준으로 회원 목록을 빠르게 조회하기 위한 인덱스
                @Index(name = "IDX_tag_code", columnList = "tag_code")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberTagEntity {

    // 고유 아이디 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관된 멤버의 코드 FK
    @Column(name = "member_code", columnDefinition = "VARCHAR(36)", nullable = false)
    private String memberCode;

    // 연관된 태그의 코드 FK
    @Column(name = "tag_code", columnDefinition = "VARCHAR(36)", nullable = false)
    private String tagCode;

    @Builder
    public MemberTagEntity(String memberCode, String tagCode) {
        this.memberCode = memberCode;
        this.tagCode = tagCode;
    }

    // 정적 팩토리 메서드로 생성 로직 캡슐화
    public static MemberTagEntity create(String memberCode, String tagCode) {
        return new MemberTagEntity(null, memberCode, tagCode);
    }
}
