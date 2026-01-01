# Module Documentation

## app

### Purpose
애플리케이션 진입점 및 전역 설정

### Responsibilities
- Application 클래스 정의
- Hilt 앱 설정 (`@HiltAndroidApp`)
- Navigation 그래프 설정 (ChordNavHost)
- 앱 테마 적용

### Dependencies
- `feature-home`
- `feature-onboarding`
- `feature-auth`
- `feature-setup`
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

## feature-onboarding

### Purpose
온보딩 플로우 제공 (신규 사용자 서비스 소개)

### Responsibilities
- 서비스 소개 슬라이드 UI (OnboardingScreen)
- 온보딩 상태 관리 (OnboardingViewModel)
- 온보딩 완료 처리 및 네비게이션

### Dependencies
- `core-domain`
- `core-ui`

### Key Classes/Functions
| Class | Purpose |
|-------|---------|
| `OnboardingScreen` | 온보딩 화면 Composable (HorizontalPager 기반) |
| `OnboardingViewModel` | 페이지 상태 및 완료 처리 관리 |
| `OnboardingUiState` | 현재 페이지, 총 페이지, 완료 상태 |
| `OnboardingPage` | 개별 슬라이드 페이지 컴포넌트 |
| `PageIndicator` | 페이지 인디케이터 컴포넌트 |

---

## feature-auth

### Purpose
사용자 인증 (로그인/회원가입) 기능 제공

### Responsibilities
- 로그인 화면 UI (LoginScreen)
- 회원가입 화면 UI (SignUpScreen)
- 인증 상태 관리 (LoginViewModel, SignUpViewModel)
- 입력 유효성 검사
- 인증 관련 네비게이션

### Dependencies
- `core-domain`
- `core-data`
- `core-ui`

### Key Classes/Functions
| Class | Purpose |
|-------|---------|
| `LoginScreen` | 로그인 화면 Composable |
| `LoginViewModel` | 로그인 상태 관리 |
| `SignUpScreen` | 회원가입 화면 Composable |
| `SignUpViewModel` | 회원가입 상태 및 유효성 검사 |
| `AuthTextField` | 비밀번호 토글 지원 입력 필드 컴포넌트 |

---

## feature-setup

### Purpose
신규 사용자 초기 설정 플로우 (매장 정보, 메뉴 등록)

### Responsibilities
- 매장 정보 입력 화면 (StoreInfoScreen)
- 메뉴 입력 화면 (MenuEntryScreen)
- 메뉴 관리 화면 (MenuManagementScreen)
- 설정 완료 화면 (SetupCompleteScreen)
- 초기 설정 네비게이션 그래프

### Dependencies
- `core-domain`
- `core-ui`

### Key Classes/Functions
| Class | Purpose |
|-------|---------|
| `StoreInfoScreen` | 매장 정보 입력 (매장명, 위치, 직원 수) |
| `MenuEntryScreen` | 메뉴 입력 (카테고리, 가격, 재료, 제조시간) |
| `MenuManagementScreen` | 등록된 메뉴 목록 관리 |
| `SetupCompleteScreen` | 초기 설정 완료 화면 |
| `SetupTextField` | 설정 화면용 입력 필드 컴포넌트 |
| `SetupDropdown` | 드롭다운 선택 컴포넌트 |

### Navigation Flow
```
로그인 "처음이신가요?" → StoreInfo → MenuManagement ↔ MenuEntry → SetupComplete → Home
```

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

### Key Classes/Functions
| Class | Purpose |
|-------|---------|
| `OnboardingRepositoryImpl` | DataStore 기반 온보딩 상태 저장 |
| `AuthRepositoryImpl` | DataStore 기반 인증 상태 관리 (MVP Mock) |
| `DataModule` | Hilt 모듈 (Repository 바인딩) |

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

### Key Classes/Functions
| Class | Purpose |
|-------|---------|
| `OnboardingRepository` | 온보딩 완료 상태 관리 인터페이스 |
| `AuthRepository` | 인증 상태 관리 인터페이스 |
| `User` | 사용자 도메인 모델 |
| `AuthToken` | 인증 토큰 모델 |
| `AuthResult` | 인증 결과 sealed interface |
| `AuthState` | 인증 상태 (Loading, Authenticated, Unauthenticated) |

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

*Last Updated: 2026-01-02*
