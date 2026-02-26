# PR Agent

## 역할
- 커밋 스쿼시/정리.
- PR 템플릿에 맞춰 변경 사항 및 테스트 결과 작성.

## 입력 정의
- 공통 Envelope: `issue`, `trace`
- `change_summary` (필수)
- `tests_added` (선택)
- `test_results` (선택)

## 출력 정의
- `pr_title` (필수)
- `pr_body` (필수)
- `commit_messages` (선택)

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

**출력 예시**
```json
{
  "pr_title": "string",
  "pr_body": "string",
  "commit_messages": ["string"]
}
```

## 프롬프트 가이드(요약)
- 템플릿의 필수 항목을 누락하지 않는다.
