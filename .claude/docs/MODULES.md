# Module Documentation

## app

### Purpose
애플리케이션 진입점 및 전역 설정

### Responsibilities
- Application 클래스 정의
- Hilt 앱 설정 (`@HiltAndroidApp`)
- Navigation 그래프 설정
- 앱 테마 적용

### Dependencies
- `feature-home`
- `core-ui`

---

## feature-home

### Purpose
홈 화면 기능 제공

### Responsibilities
- 홈 화면 UI (HomeScreen)
- 홈 화면 상태 관리 (HomeViewModel)
- 홈 관련 네비게이션

### Dependencies
- `core-domain`
- `core-ui`

---

## core-common

### Purpose
프로젝트 전역에서 사용하는 공통 유틸리티

### Responsibilities
- 공통 확장 함수
- 유틸리티 클래스
- 공통 상수 정의

### Dependencies
- None (최하위 모듈)

---

## core-data

### Purpose
데이터 레이어 구현

### Responsibilities
- Repository 구현체
- DataSource 정의 (Local/Remote)
- DTO 정의 및 매핑
- Hilt 모듈 제공

### Dependencies
- `core-domain`
- `core-common`

---

## core-domain

### Purpose
비즈니스 로직 및 도메인 모델

### Responsibilities
- Entity 정의 (비즈니스 모델)
- UseCase 정의 (비즈니스 로직)
- Repository 인터페이스 정의

### Dependencies
- `core-common`

---

## core-ui

### Purpose
공유 UI 컴포넌트 및 테마

### Responsibilities
- 공통 Composable 컴포넌트
- 앱 테마 정의 (Color, Typography)
- UI 관련 유틸리티

### Dependencies
- `core-common`

---

## build-logic/convention

### Purpose
공통 Gradle 빌드 설정

### Responsibilities
- Convention Plugin 정의
- 버전 카탈로그 적용
- 공통 빌드 설정 (SDK 버전, 컴파일 옵션)

### Plugins Provided
| Plugin ID | Purpose |
|-----------|---------|
| `chord.android.application` | App 모듈 설정 |
| `chord.android.library` | Library 모듈 설정 |
| `chord.android.compose` | Compose 설정 |
| `chord.android.hilt` | Hilt DI 설정 |
| `chord.kotlin.library` | 순수 Kotlin 모듈 |

---
*Last Updated: 2025-12-31*
