# Sync Docs Skill

세션 작업 완료 후 프로젝트 문서를 `.claude/docs/`에 동기화하는 스킬입니다.

---

## When to Trigger

이 스킬은 다음 상황에서 실행합니다:

1. **세션 종료 시**: 사용자가 "문서 동기화", "sync docs", "문서 업데이트" 요청 시
2. **주요 작업 완료 후**: 아키텍처 변경, 새로운 기능 구현, 중요한 리팩토링 완료 시
3. **사용자 요청 시**: `/sync-docs` 커맨드 또는 명시적 요청 시

---

## Documentation Structure

`.claude/docs/` 디렉토리 구조:

```
.claude/docs/
├── ARCHITECTURE.md      # 프로젝트 아키텍처 개요
├── CONVENTIONS.md       # 코드 컨벤션 및 패턴
├── MODULES.md           # 모듈별 설명 (구현된 모듈만)
└── REQUIREMENTS.md      # 요구사항 문서
```

---

## Sync Scope

`/sync-docs` 실행 시 동기화 대상:

1. **`.claude/docs/`** - 프로젝트 문서
2. **`.claude/skills/sync-docs/SKILL.md`** - 이 스킬 파일 자체

---

## Workflow

### Step 1: 현재 문서 상태 확인

```
1. .claude/docs/ 디렉토리의 기존 문서 파일들 읽기
2. .claude/skills/sync-docs/SKILL.md 읽기
3. 마지막 업데이트 시점 파악
```

### Step 2: 변경 사항 분석

```
1. 이번 세션에서 수행한 작업 목록 수집 (todoread 활용)
2. 변경된 파일들 분석 (git diff 또는 작업 기록)
3. 아키텍처/패턴 변경 사항 식별
4. 실제 구현된 코드 변경만 문서화 대상으로 식별
5. REQUIREMENTS.md 필수 검토: Implementation Details 섹션 업데이트 필요 여부 확인
```

### Step 3: 문서 업데이트

각 문서별 업데이트 기준:

#### ARCHITECTURE.md
- 새로운 모듈/레이어 **구현** 시
- 의존성 구조 **실제 변경** 시
- 주요 컴포넌트 **실제** 추가/제거 시
- 계획만 있는 경우 업데이트 하지 않음

#### CONVENTIONS.md
- 새로운 코딩 패턴 **실제 도입** 시
- 기존 컨벤션 변경 시
- 네이밍 규칙 업데이트 시

#### MODULES.md
- 새로운 모듈 **실제 구현 완료** 시에만 추가
- 모듈 책임/역할 **실제 변경** 시
- **계획(Planned) 상태의 모듈은 절대 추가하지 않음**
- **코드 블록 사용 금지**

#### REQUIREMENTS.md (필수 검토)
- **항상 검토 필수**: 구현된 기능의 Implementation Details 섹션 업데이트
- 요구사항 분석 완료 시
- 새로운 기능 요구사항 추가 시
- 기존 기능 구현 완료 시 Status 변경 (Planned/In Progress → Done)
- 구현된 컴포넌트 목록 업데이트
- **코드 블록 사용 금지**

> **코드 블록 규칙**: 코드 스니펫은 CONVENTIONS.md에만 작성. MODULES.md와 REQUIREMENTS.md는 설명/스펙만 기술.

#### SKILL.md (이 파일)
- 문서 구조 변경 시
- 워크플로우 변경 시
- 새로운 규칙 추가/삭제 시

### Step 4: 검증 및 요약

```
1. 문서 내용 일관성 검증
2. 마크다운 포맷 검증
3. 변경 사항 요약 출력
4. (선택) 커밋 제안 - 사용자 확인 후
```

---

## Document Templates

### ARCHITECTURE.md Template

```markdown
# Project Architecture

## Overview
[프로젝트 목적과 핵심 아키텍처 설명]

## Layer Structure
[레이어별 책임과 의존성 방향]

## Tech Stack
[사용 기술 스택과 선택 이유]

---
*Last Updated: YYYY-MM-DD*
```

### CONVENTIONS.md Template

```markdown
# Code Conventions

## Naming
[네이밍 규칙]

## Architecture Patterns
[사용하는 아키텍처 패턴]

## File Structure
[파일/폴더 구조 규칙]

## Best Practices
[프로젝트별 베스트 프랙티스]

---
*Last Updated: YYYY-MM-DD*
```

### MODULES.md Template

```markdown
# Module Documentation

## [Module Name]

### Purpose
[모듈의 목적]

### Responsibilities
[모듈의 책임]

### Dependencies
[의존하는 다른 모듈]

### Key Classes/Functions
[주요 클래스/함수 목록]

---
```

---

## Implementation Notes

### DO
- **실제 구현된 코드**에 기반한 문서화만 수행
- 코드베이스 분석 후 정확한 정보 기록
- 기존 문서 스타일 유지
- 변경 사항만 업데이트 (전체 재작성 X)
- 날짜 기록 필수

### DO NOT
- 추측으로 문서 작성 금지
- 코드 읽지 않고 문서화 금지
- **계획(Planned) 상태를 구현 완료처럼 문서화 금지**
- 사용자 확인 없이 커밋 금지
- 존재하지 않는 문서 파일 참조 금지

---

## Integration with AGENTS.md

문서 동기화 후 `AGENTS.md`에 다음을 추가하거나 업데이트:

```markdown
## External File Loading

CRITICAL: 다음 문서들은 작업 시 참고하세요:
- 아키텍처 이해: @.claude/docs/ARCHITECTURE.md
- 코드 컨벤션: @.claude/docs/CONVENTIONS.md
- 모듈 설명: @.claude/docs/MODULES.md
- 요구사항: @.claude/docs/REQUIREMENTS.md
```

---

## Example Usage

```
User: 문서 동기화해줘

Agent:
1. .claude/docs/ 및 .claude/skills/sync-docs/SKILL.md 확인
2. 이번 세션 작업 분석 (실제 구현된 것만)
3. 관련 문서 업데이트
4. 변경 사항 요약 출력
5. 커밋 여부 확인
```

---

*Last Updated: 2026-01-13*
