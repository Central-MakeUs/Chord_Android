# Project Architecture

## Overview

Chord는 **"수익 전략 코치 (Profit Strategy Coach)"** Android 애플리케이션으로, 소상공인을 위한 메뉴별 수익성 분석 및 AI 기반 전략 추천 서비스입니다. Clean Architecture와 MVVM 패턴을 따르는 멀티 모듈 구조입니다.

## Layer Structure

```
app/                        # Application layer - DI 설정, 앱 진입점
├── feature/                # Feature modules (화면별 분리)
│   ├── feature-home/       # 홈 화면 모듈
│   ├── feature-onboarding/ # 온보딩 플로우
│   ├── feature-auth/       # 로그인/회원가입
│   └── feature-setup/      # 초기 설정 (매장정보, 메뉴등록)
└── core/                   # Core modules (공유 기능)
    ├── core-common/        # 공통 유틸리티
    ├── core-data/          # 데이터 레이어 (Repository 구현)
    ├── core-domain/        # 도메인 레이어 (UseCase, Entity)
    └── core-ui/            # UI 컴포넌트 (Compose)
```

## Dependency Direction

```
feature-* → core-domain ← core-data
    ↓           ↓            ↓
  core-ui   core-common  core-common
```

- Feature 모듈은 core-domain에만 의존
- core-data는 core-domain의 인터페이스를 구현
- 역방향 의존성 금지

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

---
*Last Updated: 2026-01-02*
