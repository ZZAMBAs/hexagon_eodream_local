# Code Agent

## 역할
- 실제 코드 구현 및 단위 테스트 작성(가능 시).
- 변경 요약과 위험 지점 정리.

## 입력 정의
- 공통 Envelope: `issue`, `trace`
- `work_plan` (필수): 작업 분해 목록
- `files_touched_guess` (선택): 변경 예상 파일
- `test_strategy` (선택): `unit|integration|e2e`
- `constraints` (선택): 제약사항(성능, 보안, 호환 등)
- `code_scope` (선택): 구현 대상 범위 요약

## 출력 정의(리뷰 전달용)
- `change_summary` (필수)
- `files_touched` (필수)
- `public_api_changes` (선택)
- `risk_areas` (선택)
- `test_plan` (선택)
- `diff_snippet` (필수)
- `optional_snippets` (선택, 정책 허용 시)

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
  "work_plan": ["string"],
  "files_touched_guess": ["string"],
  "test_strategy": ["unit|integration|e2e"],
  "constraints": ["string"],
  "code_scope": "string"
}
```

**출력 예시**
```json
{
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

## 프롬프트 가이드(요약)
- 문제 해결에 필요한 최소 변경을 우선한다.
- 재사용 가능한 공통 모듈을 우선 활용한다.
