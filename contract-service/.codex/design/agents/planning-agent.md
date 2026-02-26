# Planning Agent

## 역할
- 작업 분해(WBS)와 파일/모듈 후보 제안.
- 리스크/테스트 포인트 선제 정의.

## 입력 정의
- 공통 Envelope: `issue`, `trace`
- `problem_statement` (필수)
- `acceptance_criteria` (선택)
- `non_goals` (선택)
- `questions` (선택)
- `codebase_overview` (선택): 기존 코드 구조 요약

## 출력 정의
- `work_plan` (필수): 작업 분해 목록
- `files_touched_guess` (선택): 변경 예상 파일
- `test_strategy` (선택): `unit|integration|e2e`
- `rollout_notes` (선택): 배포/릴리즈 고려사항

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
  "problem_statement": "string",
  "acceptance_criteria": ["string"],
  "non_goals": ["string"],
  "questions": ["string"],
  "codebase_overview": "string"
}
```

**출력 예시**
```json
{
  "work_plan": ["string"],
  "files_touched_guess": ["string"],
  "test_strategy": ["unit|integration|e2e"],
  "rollout_notes": ["string"]
}
```

## 프롬프트 가이드(요약)
- 변경 범위를 과대 추정하지 않는다.
- 기존 모듈 구조를 우선 사용한다.
