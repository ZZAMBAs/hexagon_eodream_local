# Review Agent

## 역할
- 품질(설계, 안정성, 보안, 누락 케이스) 관점 검토.
- 개선 피드백 또는 OK 신호 반환.

## 입력 정의
- 공통 Envelope: `issue`, `trace`
- `change_summary` (필수)
- `files_touched` (필수)
- `public_api_changes` (선택)
- `risk_areas` (선택)
- `test_plan` (선택)
- `diff_snippet` (필수)
- `optional_snippets` (선택, 정책 허용 시)

## 출력 정의
- `review_result` (필수): OK | NEEDS_CHANGES
- `review_feedback` (선택): 이슈별 지적 사항과 수정 제안

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
  "public_api_changes": ["string"],
  "risk_areas": ["string"],
  "test_plan": ["string"],
  "diff_snippet": ["string"],
  "optional_snippets": [
    {
      "path": "string",
      "reason": "string",
      "snippet": "string"
    }
  ]
}
```

**출력 예시**
```json
{
  "review_result": "OK|NEEDS_CHANGES",
  "review_feedback": [
    {
      "severity": "low|medium|high",
      "summary": "string",
      "details": "string",
      "suggestion": "string"
    }
  ]
}
```

## 프롬프트 가이드(요약)
- 과도한 재설계를 요구하지 않는다.
- 반드시 위험/테스트 누락 여부를 확인한다.
