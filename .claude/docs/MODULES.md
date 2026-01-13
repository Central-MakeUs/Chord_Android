# Module Documentation

## app

### Purpose
애플리케이션 진입점 및 전역 설정

### Responsibilities
- Application 클래스 정의
- Hilt 앱 설정 (`@HiltAndroidApp`)
- Navigation 그래프 설정 (ChordNavHost)
- Bottom Navigation (ChordBottomNavBar)
- 앱 테마 적용

### Dependencies
- `feature-home`
- `feature-onboarding`
- `feature-auth`
- `feature-setup`
- `feature-menu`
- `core-ui`

### Key Classes/Functions
| Class | Purpose |
|-------|---------|
| `MainActivity` | Scaffold + Bottom Nav + NavHost 구성 |
| `ChordNavHost` | 전체 네비게이션 그래프 |
| `ChordBottomNavBar` | 4탭 Bottom Navigation (홈/메뉴/재료/AI코치) |

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

## feature-menu

### Purpose
메뉴 관리 기능 제공 (FR-003 구현)

### Responsibilities
- 메뉴 목록 화면 UI (MenuListScreen)
- 메뉴 상세 화면 UI (MenuDetailScreen)
- 메뉴 관리 화면 UI (MenuManagementScreen) - 메뉴 정보 수정/삭제
- 원가 분석 카드 표시
- 메뉴 상태 표시 (안전/주의/위험)
- 카테고리 필터링
- 재료 목록 표시

### Dependencies
- `core-domain`
- `core-ui`

### Key Classes/Functions
| Class | Purpose |
|-------|---------|
| `MenuListScreen` | 메뉴 목록 화면 (카테고리 칩, 상태 배지) |
| `MenuListViewModel` | 메뉴 목록 상태 관리 (Mock 데이터) |
| `MenuDetailScreen` | 메뉴 상세 화면 (원가 분석, 마진등급, 권장가격, 재료 목록) |
| `MenuDetailViewModel` | 메뉴 상세 상태 관리 |
| `MenuManagementScreen` | 메뉴 관리 화면 (이름/가격/제조시간/카테고리 수정, 삭제) |
| `MenuManagementViewModel` | 메뉴 관리 상태 관리 |
| `EditMenuNameBottomSheet` | 메뉴명 수정 바텀시트 |
| `EditPriceBottomSheet` | 가격 수정 바텀시트 |
| `EditPreparationTimeBottomSheet` | 제조시간 수정 바텀시트 |
| `MenuStatusBadge` | 안전/주의/위험 상태 배지 컴포넌트 |
| `CategoryChip` | 카테고리 필터 칩 컴포넌트 |
| `CostAnalysisCard` | 원가 분석 카드 (마진율, 총원가, 공헌이익 표시) |
| `MarginGradeCard` | 마진등급 카드 (ChordStatusBadge 포함, 가운데 정렬) |
| `RecommendedPriceSection` | 권장가격 섹션 (가격 + 메시지, 가운데 정렬) |
| `IngredientListItem` | 재료 항목 (이름+수량 Row, 가격 표시) |
| `IngredientTotalRow` | 재료 총합 Row (우측 정렬) |

### Data Models (Feature-local)
```kotlin
data class MenuItemUi(
    val id: Long,
    val name: String,
    val sellingPrice: Int,
    val costRatio: Float,
    val marginRatio: Float,
    val status: MenuStatus
)

enum class MenuStatus { SAFE, WARNING, DANGER }
```

### Navigation
| Route | Screen | Parameters |
|-------|--------|------------|
| `menu_list` | MenuListScreen | - |
| `menu_detail/{menuId}` | MenuDetailScreen | menuId: Long |
| `menu_management/{menuId}` | MenuManagementScreen | menuId: Long |

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

### Key Components
| Component | Purpose |
|-----------|---------|
| `ChordTopAppBar` | 상단 앱바 (뒤로가기 버튼, 타이틀) |
| `ChordTopAppBarWithBackTitle` | 뒤로가기 + 타이틀 상단 앱바 |
| `ChordButton` | 기본 버튼 컴포넌트 |
| `ChordLargeButton` | 전체 너비 대형 버튼 |
| `ChordOutlinedButton` | 테두리만 있는 버튼 (삭제 등 보조 액션용, Grayscale500 테두리) |
| `ChordSearchBar` | 검색 입력 필드 |
| `ChordTextField` | 텍스트 입력 필드 (Outlined/Underline 스타일 지원) |
| `ChordRadioGroup` | 라디오 버튼 그룹 (16dp row 간격) |
| `ChordBottomSheet` | 하단 시트 (skipPartiallyExpanded, navigationBars inset 지원) |
| `ChordTwoButtonDialog` | 2버튼 다이얼로그 (취소/확인) |
| `ChordOneButtonDialog` | 1버튼 다이얼로그 (확인만) |
| `ChordCheckboxItem` | 체크박스 아이템 (커스텀 ic_checkbox/ic_un_checkbox 아이콘 사용) |
| `ChordTooltip` | 툴팁 컴포넌트 (아이콘 + 버블 통합) |
| `ChordTooltipIcon` | 툴팁 아이콘 (독립 사용 가능) |
| `ChordTooltipBubble` | 툴팁 버블 (8방향 지원, 독립 사용 가능) |

### ChordTooltip Usage
```kotlin
// 통합 사용 (아이콘 클릭 시 버블 토글)
ChordTooltip(
    text = "도움말 텍스트",
    direction = TooltipDirection.UpLeft
)

// 분리 사용 (버블 위치를 자유롭게 배치)
ChordTooltipIcon(onClick = { isVisible = !isVisible })
if (isVisible) {
    ChordTooltipBubble(
        text = "도움말 텍스트",
        direction = TooltipDirection.UpLeft
    )
}

// 지원 방향: Up, UpLeft, UpRight, Down, DownLeft, DownRight, Left, Right
```

### Color System
| Category | Colors | Usage |
|----------|--------|-------|
| Primary | PrimaryBlue100~900 | 주요 액션, 활성 상태 |
| Grayscale | Grayscale100~900 | 배경, 텍스트, 보더 |
| Status | StatusSafe, StatusWarning, StatusDanger | 메뉴 상태 표시 |
| StatusBg | StatusSafeBg, StatusWarningBg, StatusDangerBg | 상태 배지 배경 |

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

*Last Updated: 2026-01-14 (core-ui: ChordTextField Underline 스타일, ChordBottomSheet edge-to-edge 지원, ChordCheckboxItem 커스텀 아이콘)*
