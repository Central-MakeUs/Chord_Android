# Commit Skill

코드 변경사항을 커밋하는 스킬입니다.

---

## When to Trigger

- 사용자가 "커밋해줘", "commit", "변경사항 커밋" 요청 시
- `/commit` 명령어 실행 시

---

## Commit Convention

### Commit Message Format
```
<type>: <subject>

<body>

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
```

### Type Prefix
| Type | Description |
|------|-------------|
| `feat` | 새로운 기능 추가 (새 파일 생성, 새 기능 구현) |
| `fix` | 버그 수정 |
| `docs` | 문서 변경 |
| `style` | 코드 포맷팅 (기능 변경 없음) |
| `refactor` | 코드 리팩토링 (기존 코드 구조 변경, UI 레이아웃 수정 등) |
| `test` | 테스트 추가/수정 |
| `chore` | 빌드, 설정 파일 변경 |

### feat vs refactor 구분
| 상황 | Type | 예시 |
|------|------|------|
| 새 파일/컴포넌트 생성 | `feat` | 새로운 ChordDialog.kt 파일 추가 |
| 기존 화면 레이아웃 수정 | `refactor` | MenuManagementScreen UI 구조 변경 |
| 새 기능 동작 추가 | `feat` | 삭제 확인 다이얼로그 기능 추가 |
| 기존 코드 구조 개선 | `refactor` | 함수 분리, 코드 정리 |
| 기존 화면에 새 UI 적용 | `refactor` | 기존 화면에 디자인 시스템 적용 |

**핵심**: 기존 파일의 구조/레이아웃을 변경하는 것은 `refactor`, 완전히 새로운 기능을 추가하는 것은 `feat`

### Subject Rules
- 한글 또는 영문 사용 (프로젝트 컨벤션 따름)
- 50자 이내
- 마침표 없음
- 명령문 형태 (Add, Fix, Update 등)

---

## Workflow

### Step 1: 변경사항 확인
```bash
git status
git diff --stat
```

### Step 2: 커밋 분리 판단

다음 기준으로 커밋을 분리:

| 분리 기준 | 예시 |
|----------|------|
| 모듈별 | core-ui, feature-setup 각각 커밋 |
| 기능별 | 새 컴포넌트, 화면 수정, 문서 업데이트 |
| 유형별 | feat, fix, docs 분리 |

### Step 3: 파일 스테이징
```bash
git add <files>
```

### Step 4: 커밋
```bash
git commit -m "$(cat <<'EOF'
<type>: <subject>

<body>

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"
```

### Step 5: 결과 확인
```bash
git log -3 --oneline
git status --short
```

---

## Excluded Files

커밋에서 제외할 파일:
- `.idea/` - IDE 설정
- `*.iml` - IntelliJ 모듈 파일
- `local.properties` - 로컬 환경 설정
- `.gradle/` - Gradle 캐시

---

## Examples

```
사용자: 커밋해줘

→ git status 확인
→ 모듈/기능별로 파일 그룹화
→ 각 그룹별로 별도 커밋 생성
```

---

## DO NOT

- `.idea/`, `.gradle/` 등 IDE/빌드 파일 커밋 금지
- 사용자 확인 없이 `git push` 금지
- `--force`, `--amend` (최근 커밋이 아닌 경우) 금지
- 비밀번호, API 키 등 민감 정보 포함 파일 커밋 금지

---

*Last Updated: 2026-01-13 (feat vs refactor 구분 가이드 추가)*
