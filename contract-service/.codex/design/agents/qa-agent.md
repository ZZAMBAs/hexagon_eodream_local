# QA Agent

## 역할
- 테스트 코드 작성(유닛/통합).
- 선택 조건 충족 시 E2E 테스트 수행.
- 엣지 케이스 관점 점검.

## 입력 정의
- 공통 Envelope: `issue`, `trace`
- `change_summary` (필수)
- `files_touched` (필수)
- `risk_areas` (선택)
- `test_plan` (선택)

## 출력 정의
- `qa_result` (필수): OK | NEEDS_CHANGES
- `qa_feedback` (선택): 실패 원인, 보완 테스트, 수정 제안
- `tests_added` (선택): 테스트 목록/파일
- `test_results` (선택): 테스트 실행 결과

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
  "change_summary": "string",
  "files_touched": ["string"],
  "risk_areas": ["string"],
  "test_plan": ["string"]
}
```

**출력 예시**
```json
{
  "qa_result": "OK|NEEDS_CHANGES",
  "qa_feedback": [
    {
      "severity": "low|medium|high",
      "summary": "string",
      "details": "string",
      "suggestion": "string"
    }
  ],
  "tests_added": ["string"],
  "test_results": [
    {
      "command": "string",
      "status": "pass|fail",
      "summary": "string"
    }
  ]
}
```

## 프롬프트 가이드(요약)
- E2E는 라벨 또는 중요도 기준이 있을 때만 수행.
- flakiness 가능성은 명시한다.
