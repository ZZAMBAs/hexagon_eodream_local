# LangChain/LangGraph 설계(초안)

## 목표
- Issue 기반 자동화 흐름을 상태기계로 구현한다.
- 에이전트 간 전달은 메타데이터(JSON)만으로 최소화한다.
- 실패/재시도/수동 개입 지점을 명확히 한다.

## 권장 구성
- LangGraph: 전체 상태 전이(노드/엣지/가드)
- LangChain: 각 에이전트 실행(프롬프트 + 도구 호출)

## 상태 모델(예시)
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
  "artifacts": {
    "triage": {},
    "plan": {},
    "code": {},
    "review": {},
    "qa": {},
    "pr": {}
  },
  "flags": {
    "needs_user_input": false,
    "review_ok": false,
    "qa_ok": false
  },
  "limits": {
    "max_iterations": 3,
    "current_iteration": 0
  }
}
```

## 노드 정의
- `triage`: Issue Triage Agent 실행
- `plan`: Planning Agent 실행
- `implement`: Code Agent 실행
- `review`: Review Agent 실행
- `qa`: QA Agent 실행
- `pr`: PR Agent 실행
- `user_input`: 질문이 있을 때 사용자 확인
- `fail_safe`: 반복 초과/장애 처리

## 전이 조건(요약)
- `triage` → `user_input`: `questions`가 비어있지 않을 때
- `user_input` → `triage`: 질문 답변 반영 후 재시도
- `triage` → `plan`: 질문이 없을 때
- `plan` → `implement`: 항상
- `implement` → `review`: 항상
- `review` → `implement`: `review_result = NEEDS_CHANGES`
- `review` → `qa`: `review_result = OK`
- `qa` → `implement`: `qa_result = NEEDS_CHANGES`
- `qa` → `pr`: `qa_result = OK`
- `pr` → `end`: PR 생성 완료
- 반복 횟수 초과 시 `fail_safe`

## 가드/루프 제어
- `current_iteration` 증가 규칙
  - `review` 또는 `qa`에서 `NEEDS_CHANGES` 발생 시 증가
- `current_iteration >= max_iterations` 이면 `fail_safe`로 전이
- `fail_safe`는 사용자 개입 또는 종료를 요구

## E2E 실행 정책(라벨 기반)
- `labels`에 `needs-e2e`가 있을 때만 E2E 실행 노드 활성화
- 기본은 `unit|integration`만 수행

## 에러 처리 전략
- 도구 호출 실패: 1회 재시도 후 `fail_safe`
- JSON 파싱 실패: 해당 노드 재실행 (최대 1회)
- 외부 API 실패(GitHub/Notion): 대체 경로 또는 사용자 알림

## 관측/로깅
- 모든 노드 실행 시작/종료 시 `trace` 기록
- `artifacts`에 각 노드 출력 저장
- 실패 시 `error_summary` 필드에 원인 저장

## 확장 포인트
- `doc_agent`(릴리즈노트/README/Notion) 노드 추가
- `security_review` 노드 추가 (고위험 라벨에서만 실행)
