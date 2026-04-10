# Project Architecture

## Overview

Chord는 **"수익 전략 코치 (Profit Strategy Coach)"** Android 애플리케이션으로, 소상공인을 위한 메뉴별 수익성 분석 및 AI 기반 전략 추천 서비스입니다. Clean Architecture와 MVVM 패턴을 따르는 멀티 모듈 구조입니다.

## Layer Structure

```
app/                        # Application layer - DI 설정, 앱 진입점, Bottom Navigation
├── feature/                # Feature modules (화면별 분리)
│   ├── feature-home/       # 홈 화면 모듈
│   ├── feature-onboarding/ # 온보딩 플로우
│   ├── feature-auth/       # 로그인/회원가입
│   ├── feature-setup/      # 초기 설정 (매장정보, 메뉴등록)
│   ├── feature-menu/       # 메뉴 관리 (목록, 상세, 원가분석)
│   └── feature-menuadd-shared/ # 메뉴 등록 공통 플로우 (search/detail/ingredient/confirm)
└── core/                   # Core modules (공유 기능)
    ├── core-common/        # 공통 유틸리티
    ├── core-data/          # 데이터 레이어 (Repository 구현)
    ├── core-domain/        # 도메인 레이어 (UseCase, Entity)
    └── core-ui/            # UI 컴포넌트 (Compose), 테마 (Color, Typography)
```

## Dependency Direction

```
feature-setup ─┐
feature-menu ──┼→ feature-menuadd-shared → core-domain ← core-data
app────────────┘              ↓               ↓            ↓
                           core-ui       core-common  core-common
```

- Feature 모듈은 core-domain에만 의존
- core-data는 core-domain의 인터페이스를 구현
- 역방향 의존성 금지
- 메뉴 등록 공통 플로우는 `feature-menuadd-shared`에서 관리하고, `feature-setup`/`feature-menu`는 진입/완료 분기만 담당

## Tech Stack

| Category | Technology | Version |
|----------|------------|---------|
| Language | Kotlin | 2.0.21 |
| UI | Jetpack Compose | BOM 2024.09.00 |
| DI | Hilt | 2.56.1 |
| Architecture | Clean Architecture + MVVM | - |
| Build | Gradle KTS + Convention Plugins | - |
| Navigation | Compose Navigation | 2.9.0 |
| Async | Kotlin Coroutines + Flow | 1.10.2 |

## Build Logic

`build-logic/convention/`에서 공통 빌드 설정 관리:

| Plugin ID | Purpose |
|-----------|---------|
| `chord.android.application` | App 모듈용 |
| `chord.android.library` | Library 모듈용 |
| `chord.android.feature` | Feature 모듈용 (library + hilt + compose) |
| `chord.android.compose` | Compose 설정 |
| `chord.android.hilt` | Hilt DI 설정 |
| `chord.jvm.library` | 순수 Kotlin 모듈 |

## Main Screen Architecture

앱의 메인 화면은 Scaffold 기반 Bottom Navigation 구조:

```kotlin
Scaffold(
    bottomBar = { ChordBottomNavBar(...) }
) { paddingValues ->
    ChordNavHost(navController, modifier = Modifier.padding(paddingValues))
}
```

### Bottom Navigation Tabs
| Tab | Route | Feature Module |
|-----|-------|----------------|
| 홈 | `home` | feature-home |
| 메뉴 | `menu_list` | feature-menu |
| 재료 | (planned) | feature-ingredient |
| AI코치 | (planned) | feature-strategy |

## Shared Menu Add Flow

메뉴 등록의 `MenuSearch → MenuDetail → IngredientInput → MenuConfirm` 단계는 `feature-menuadd-shared`에 공통 구현으로 위치한다.

- `feature-setup`: 온보딩 메뉴 등록 진입점 + 완료 후 `SetupComplete`
- `feature-menu`: 일반 메뉴 추가 진입점 + 완료 후 `MenuAddComplete`
- `feature-menuadd-shared`: 공통 flow owner, 화면, 상태, 단일 메뉴 등록 정책

현재 메뉴 등록 정책은 **단일 메뉴 등록만 허용**한다.

---
*Last Updated: 2026-04-10 (feature-menuadd-shared 공통 메뉴 등록 플로우 및 단일 메뉴 정책 반영)*
