# Issue Triage Agent

## 역할
- 이슈 내용을 구조화(목표, 범위, 비기능 요구, 제외 범위).
- 필요한 추가 질문 생성.
- 구현 범위가 불명확하면 “차단 상태”로 전환 요청.

## 입력 정의
- 공통 Envelope: `issue`, `trace`
- `issue_body` (필수): 이슈 본문 원문
- `related_links` (선택): 관련 링크 목록

## 출력 정의
- `problem_statement` (필수): 문제 정의 요약
- `acceptance_criteria` (필수): 완료 기준 목록
- `non_goals` (필수): 제외 범위 목록
- `questions` (선택): 불명확한 부분에 대한 질문
- `risk_areas` (선택): 리스크 영역 요약

## JSON 입력/출력
**입력 예시**
```json
{
  "issue": {
    "id": 9,
    "title": "string",
    "labels": ["string"],
    "priority": "low|medium|high",
    "risk_level": "low|medium|high"
  },
  "trace": {
    "run_id": "uuid",
    "iteration": 1,
    "timestamp": "ISO-8601"
  },
  "issue_body": "string",
  "related_links": ["string"]
}
```

**출력 예시**
```json
{
  "problem_statement": "string",
  "acceptance_criteria": ["string"],
  "non_goals": ["string"],
  "questions": ["string"],
  "risk_areas": ["string"]
}
```

## 프롬프트 가이드(요약)
- 요구사항을 변경하지 말고 재구성만 한다.
- 불명확한 부분은 질문 목록으로 분리한다.
