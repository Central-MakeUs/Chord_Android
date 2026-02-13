# Requirements

## Overview

프로젝트 요구사항 문서. 특정 툴에 종속되지 않으며 OpenAI Codex 등 AI 어시스턴트를 활용한 요구사항 정리 결과를 단일 출처로 유지한다.

### How to Use
1. 새 기능 논의 시 문제, 사용자, 성공 기준을 먼저 확정
2. 요구사항 정리 완료 후 "요구사항 문서화해줘" 요청
3. 문서 갱신 시 변경 범위와 상태(Planned/In Progress/Done)를 함께 업데이트

### Status Legend
| Status | Description |
|--------|-------------|
| `Planned` | 요구사항 정의 완료, 개발 대기 |
| `In Progress` | 개발 진행 중 |
| `Done` | 개발 완료 |
| `Cancelled` | 취소됨 |

---

<!-- 새 Feature 섹션은 이 아래에 추가 -->

## 1. 수익 전략 코치 (Profit Strategy Coach) - 제품 개요

### Status
`Planned`

### Overview
소상공인(특히 카페/식당 사장님)이 메뉴와 가격만 입력하면 자동으로 수익성, 공헌이익, 손익분기점을 계산하고, AI 기반 전략 가이드(유지/조정/제거 메뉴 추천)를 제공하는 모바일 앱.

### Target Users
| 사용자 유형 | 특징 | Pain Points |
|------------|------|-------------|
| 소상공인 | 카페/식당/소매점 운영 | 복잡한 수익성 계산 어려움 |
| 1인 창업자 | 모든 업무 혼자 처리 | 시간 부족, 전문 지식 부족 |
| 예비 창업자 | 사업 계획 수립 중 | 수익 구조 이해 필요 |

### Market Gap
기존 앱들(캐시노트, 앱트래커, 토스/카카오 비즈니스)은 매출 기록, 정산, 부가세, 알림, 대출, 마케팅에 집중. **메뉴별 수익성 분석 및 전략 추천 기능 부재**.

### Success Criteria
- 메뉴 1개 입력부터 분석 결과까지 3분 이내
- 월 활성 사용자(MAU) 기준 80% 재방문율
- 전략 가이드 실행률 40% 이상
- 앱스토어 평점 4.5점 이상

---

## 2. 온보딩 (Onboarding)

### Status
`In Progress`

### Overview
사용자가 앱을 처음 실행할 때 원가 분석의 중요성을 이해하고, 최소 1개 이상의 메뉴를 등록할 수 있도록 안내하는 플로우.

### User Stories
- As a 신규 사용자, I want to 앱의 가치를 빠르게 이해하고 싶다 so that 사용 여부를 판단할 수 있다.
- As a 예비 창업자, I want to 원가 분석 기초를 배우고 싶다 so that 사업 계획에 반영할 수 있다.
- As a 기존 사장님, I want to 빠르게 메뉴를 등록하고 싶다 so that 바로 분석 결과를 볼 수 있다.

### Functional Requirements
| ID | Requirement | Priority | Status | Acceptance Criteria |
|----|-------------|----------|--------|---------------------|
| FR-002-001 | 서비스 소개 화면 표시 | High | ✅ Done | 스와이프 가능한 3장의 소개 슬라이드 |

### Implementation Details (FR-002-001)

#### 구현된 슬라이드 내용
1. **Page 1**: "원가율이 만드는 것" - 원가율 5%에 따라 월수익 100~300만원의 차이가 발생해요
2. **Page 2**: "코치코치가 대신 해드려요" - 어렵고 귀찮은 원가 관리부터 우리 가게 전략 설계까지!
3. **Page 3**: "메뉴와 재료만 알려주세요" - 바로 수익 진단을 내려드릴게요. 복잡한건 코치코치에게 맡기세요

#### 구현된 컴포넌트
- `OnboardingScreen`: HorizontalPager 기반 슬라이드 UI
- `OnboardingPage`: 개별 페이지 컴포넌트 (제목 + 설명)
- `PageIndicator`: 현재 페이지 위치 표시
- `OnboardingViewModel`: 페이지 상태 및 완료 처리 관리
- `OnboardingRepository`: DataStore 기반 완료 상태 저장

### Non-Functional Requirements
| Category | Requirement | Status |
|----------|-------------|--------|
| UX | 온보딩 전체 플로우 5분 이내 완료 가능 | ✅ (현재 구현된 소개 화면은 1분 이내) |

### Technical Considerations
- ✅ OnboardingViewModel에서 단계별 상태 관리
- ✅ DataStore를 사용한 온보딩 완료 상태 저장 (OnboardingRepository)

### Open Questions
- [ ] 교육 콘텐츠 분량 및 형식 확정 필요
- [ ] 업종별 벤치마크 데이터 출처 확보 필요
- [ ] 온보딩 스킵 시 기본 매장 정보 처리 방법

---

## 3. 메뉴 관리 (Menu Management)

### Status
`In Progress`

### Overview
사용자가 판매하는 모든 메뉴를 등록, 수정, 삭제하고, 각 메뉴의 원가 분석 결과 및 AI 수익 전략을 확인할 수 있는 기능.

### User Stories
- As a 사장님, I want to 메뉴를 쉽게 등록하고 싶다 so that 모든 메뉴의 수익성을 파악할 수 있다.
- As a 사장님, I want to 메뉴별 원가와 마진을 한눈에 보고 싶다 so that 전략적 의사결정을 할 수 있다.
- As a 사장님, I want to AI 추천 전략을 받고 싶다 so that 수익을 개선할 수 있다.

### Functional Requirements

#### 메뉴 목록 (1st Depth)
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-003-001 | 전체 메뉴 리스트 표시 | High | 메뉴명, 판매가, 원가율 요약 표시 |
| FR-003-002 | 카테고리별 필터링 | Medium | 음료/디저트/푸드 등 카테고리 필터 |
| FR-003-003 | 마진 등급별 정렬 | Medium | A~D 등급 기준 정렬 옵션 |
| FR-003-004 | 메뉴 검색 | Low | 메뉴명 검색 기능 |
| FR-003-005 | 메뉴 추가 버튼 | High | FAB 또는 상단 버튼으로 추가 화면 진입 |

#### 메뉴 상세 (2nd Depth)
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-003-006 | 원가 분석 요약 카드 | High | 총원가, 원가율, 공헌이익, 권장가격, 마진등급 표시 |
| FR-003-007 | 수익 전략 실행 섹션 | High | AI 추천(가격조정, 원가이상감지, 레시피변경, 단종권고) 표시 |
| FR-003-008 | 재료 관리 네비게이션 | High | "4개 재료 · 총 재료원가 820원" 형태로 요약, 클릭 시 재료 편집 |
| FR-003-009 | 메뉴 기본 설정 | High | 카테고리 설정, 가격 설정, 삭제 기능 |
| FR-003-010 | 원가 상세 분해 보기 | Medium | 재료비, 포장비, 인건비, 간접비 breakdown |

#### 원가 분석 계산
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-003-011 | 총원가 계산 | High | 재료비 + 포장비 + 인건비(시간×시급) + 간접비 배분 |
| FR-003-012 | 원가율 계산 | High | (총원가 / 판매가) × 100 |
| FR-003-013 | 공헌이익 계산 | High | 판매가 - 변동비(재료비+포장비) |
| FR-003-014 | 권장가격 계산 | High | 총원가 / 목표원가율(예: 30%) |
| FR-003-015 | 마진 등급 산정 | High | A(~25%), B(26-35%), C(36-45%), D(45%~) |

### Non-Functional Requirements
| Category | Requirement |
|----------|-------------|
| Performance | 메뉴 100개 기준 목록 로딩 1초 이내 |
| UX | 원가 분석 카드는 스크롤 없이 한 화면에 표시 |
| 데이터 정합성 | 재료 가격 변경 시 관련 메뉴 원가 자동 재계산 |

### Technical Considerations
- Menu Entity와 Ingredient Entity 간 N:M 관계 (MenuIngredient 중간 테이블)
- Room Database Flow를 활용한 실시간 원가 재계산
- 계산 로직은 core-domain UseCase로 분리

### Open Questions
- [ ] 마진 등급 기준값 사용자 커스터마이징 허용 여부
- [ ] 간접비 배분 로직 (매출 비중 기반 vs 균등 배분)

### Implementation Details (2026-01-14)

#### 구현된 화면
1. **MenuListScreen (1st Depth)** - FR-003-001, FR-003-002, FR-003-005 완료
   - 전체 메뉴 리스트 표시 (메뉴명, 원가율, 마진율)
   - 카테고리별 필터링 (전체/음료/디저트)
   - 메뉴 추가 버튼 ("+ 추가")
   - 상태 배지 표시 (안전/주의/위험)

2. **MenuDetailScreen (2nd Depth)** - FR-003-006, FR-003-008 완료
   - 메뉴명 + 제조시간 배지 + 판매가 헤더
   - 원가 분석 카드 (CostAnalysisCard)
   - 마진등급 카드 (MarginGradeCard)
   - 권장가격 섹션 (RecommendedPriceSection)
   - 재료 목록 및 총 재료비 표시
   - 재료 수정 네비게이션 (화살표 아이콘)

3. **MenuManagementScreen (3rd Depth)** - FR-003-009 완료
   - 메뉴명 수정 (EditMenuNameBottomSheet)
   - 가격 수정 (EditPriceBottomSheet)
   - 제조시간 수정 (EditPreparationTimeBottomSheet)
   - 카테고리 변경 (ChordRadioGroup)
   - 메뉴 삭제 (ChordTwoButtonDialog 확인 + ChordOneButtonDialog 완료)

4. **IngredientEditScreen (3rd Depth)** - FR-003-008 상세 구현 완료
   - 메뉴별 재료 목록 표시 (ChordCheckboxItem)
   - 재료 추가 (AddIngredientBottomSheet - Underline TextField)
   - 재료 수정 (EditIngredientBottomSheet - Underline TextField)
   - 재료 삭제 (삭제 모드 토글, 다중 선택, 다이얼로그 확인/완료)
   - ChordTopAppBar 사용 (동적 타이틀, 삭제/취소 액션)

#### 구현된 컴포넌트
| Component | Purpose |
|-----------|---------|
| `MenuStatusBadge` | 안전(녹색)/주의(주황)/위험(빨강) 상태 배지 |
| `CategoryChip` | 카테고리 필터 칩 (활성/비활성 상태) |
| `CostAnalysisCard` | 3행 원가 분석 카드 (마진율, 총 원가(원가율), 공헌이익) - 24dp 간격, 100dp 최소 너비 |
| `MarginGradeCard` | 마진등급 표시 (text + ChordStatusBadge, 4dp 간격, 중앙정렬) |
| `RecommendedPriceSection` | 권장가격 표시 (가격 + 메시지, 중앙정렬) |
| `IngredientListItem` | 재료 항목 (이름+수량 Row, 가격 표시) |
| `IngredientTotalRow` | 재료 총합 Row (우측 정렬, "총" 텍스트 + 가격) |
| `IngredientItemContent` | 재료 콘텐츠 (이름, 수량+가격 같은 Row) |
| `AddIngredientBottomSheet` | 재료 추가 바텀시트 (Underline TextField) |
| `EditIngredientBottomSheet` | 재료 수정 바텀시트 (Underline TextField) |

#### CostAnalysisCard 구조
```kotlin
Column {
    CostAnalysisRow(label = "마진율", value = "XX.X%")
    CostAnalysisRow(label = "총 원가(원가율)", value = "X,XXX원 (XX.X%)")
    CostAnalysisRowWithIcon(label = "공헌이익", value = "X,XXX원")  // ic_tooltip 아이콘 포함
}
// 각 row: 24dp 간격, label 최소 너비 100dp
```

#### 추가된 색상 (core-ui/theme/Color.kt)
```kotlin
val StatusSafe = Color(0xFF34C759)      // 안전 (녹색)
val StatusWarning = Color(0xFFFF9500)   // 주의 (주황)
val StatusDanger = Color(0xFFFF3B30)    // 위험 (빨강)
val StatusSafeBg = Color(0xFFE8F5E9)    // 안전 배경
val StatusWarningBg = Color(0xFFFFF3E0) // 주의 배경
val StatusDangerBg = Color(0xFFFFEBEE)  // 위험 배경
```

#### 미구현 항목 (Phase 2)
- FR-003-003: 마진 등급별 정렬
- FR-003-004: 메뉴 검색
- FR-003-007: 수익 전략 실행 섹션
- FR-003-010: 원가 상세 분해 보기
- FR-003-011~015: 실제 원가 계산 로직 (현재 Mock 데이터)

---

## 4. 재료 관리 (Ingredient Management)

### Status
`In Progress`

### Overview
메뉴에 사용되는 모든 재료를 등록, 수정, 삭제하고, 가격 변동 이력을 관리하는 기능.

### User Stories
- As a 사장님, I want to 재료를 한번만 등록하고 여러 메뉴에서 사용하고 싶다 so that 중복 입력을 피할 수 있다.
- As a 사장님, I want to 재료 가격 변동을 기록하고 싶다 so that 원가 변화를 추적할 수 있다.
- As a 사장님, I want to 재료가 어느 메뉴에 사용되는지 보고 싶다 so that 영향 범위를 파악할 수 있다.

### Functional Requirements

#### 재료 목록 (1st Depth)
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-004-001 | 전체 재료 리스트 표시 | High | 재료명, 단위, 단가, 사용 메뉴 수 표시 |
| FR-004-002 | 카테고리별 필터링 | Medium | 식재료/포장재/소모품 등 분류 |
| FR-004-003 | 재료 검색 | Low | 재료명 검색 기능 |
| FR-004-004 | 재료 추가 버튼 | High | 모달 또는 별도 화면으로 추가 |

#### 재료 상세 (2nd Depth)
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-004-005 | 기본 정보 표시 | High | 재료명, 단위(g), 단가(25원/g), 공급처(선택) |
| FR-004-006 | 가격 변동 이력 | Medium | 날짜별 가격 변경 기록 표시 |
| FR-004-007 | 사용 메뉴 목록 | High | 이 재료를 사용하는 메뉴 리스트 |
| FR-004-008 | 가격 수정 기능 | High | 현재 가격 수정, 이전 가격 이력에 자동 저장 |
| FR-004-009 | 단위 변경 기능 | Medium | g ↔ ml ↔ 개 단위 변환 |
| FR-004-010 | 재료 삭제 기능 | High | 사용 중인 메뉴 있을 시 경고 표시 |

#### 재료 추가 (Modal/3rd Depth)
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-004-011 | 재료명 입력 | High | 필수 입력 |
| FR-004-012 | 단위 선택 | High | g, ml, 개 중 선택 |
| FR-004-013 | 단가 입력 | High | 원/단위 형식 |
| FR-004-014 | 카테고리 선택 | Medium | 식재료/포장재/소모품/기타 |
| FR-004-015 | 공급처 입력 | Low | 선택 입력 |

### Non-Functional Requirements
| Category | Requirement |
|----------|-------------|
| 데이터 정합성 | 재료 삭제 시 Soft Delete, 연결된 메뉴에서 참조 유지 |
| UX | 가격 변경 시 영향받는 메뉴 즉시 표시 |
| 이력 관리 | 최소 12개월 가격 이력 보관 |

### Technical Considerations
- Ingredient Entity에 priceHistory: List<PriceRecord> 관계 설정
- 가격 변경 시 Trigger 또는 UseCase에서 관련 메뉴 재계산 호출
- 삭제 시 isDeleted 플래그 사용 (Soft Delete)

### Open Questions
- [ ] 재료 단위 환산 로직 (100g = 100ml 가정 여부)
- [ ] 가격 이력 최대 보관 기간

### Implementation Details (2026-01-15)

#### 구현된 화면
1. **IngredientListScreen (1st Depth)** - FR-004-001, FR-004-002 완료
   - 전체 재료 리스트 표시 (재료명, 가격, 사용량)
   - 다중 필터링 (즐겨찾기, 식재료, 운영 재료) - AND 조건
   - 필터 칩 X 버튼으로 개별 해제
   - 흰색 박스 (상단 코너 24dp radius) + Divider

2. **IngredientSearchScreen** - FR-004-003 완료
   - ChordSearchBar 사용 (입력 시 삭제 버튼 표시)
   - 최근 검색어 섹션 (최대 10개, 로컬 저장)
   - 검색 결과 섹션 (원형 + 아이콘으로 추가)

3. **IngredientDetailScreen (2nd Depth)** - FR-004-005, FR-004-006, FR-004-007, FR-004-010 완료
   - 재료명 + 즐겨찾기 토글
   - 가격/공급처 정보 표시
   - 사용 메뉴 칩 목록
   - 가격 이력 타임라인 (원형 인디케이터)
   - 삭제 버튼

#### 구현된 컴포넌트
| Component | Purpose |
|-----------|---------|
| `IngredientFilterChip` | 다중 선택 필터 칩 (활성 시 X 버튼 표시) |
| `IngredientListItem` | 재료 항목 (이름, 가격, 사용량 표시) |
| `RecentSearchItem` | 최근 검색어 항목 (클릭 시 검색, X로 삭제) |
| `SearchResultItem` | 검색 결과 항목 (전체 Row 클릭 가능, 원형 + 아이콘) |
| `UsedMenuChip` | 사용 메뉴 칩 (Outlined 스타일) |
| `PriceHistoryItem` | 가격 이력 타임라인 항목 |

#### 구현된 도메인 레이어
| Class | Purpose |
|-------|---------|
| `Ingredient` | 재료 도메인 모델 (id, name, price, unit, category, isFavorite, supplier) |
| `IngredientFilter` | 필터 enum (FAVORITE, FOOD_INGREDIENT, OPERATIONAL_SUPPLY) |
| `IngredientCategory` | 카테고리 enum (FOOD_MATERIAL, OPERATIONAL) |
| `RecentSearch` | 최근 검색어 모델 |
| `IngredientRepository` | 재료 관리 인터페이스 |
| `GetIngredientListUseCase` | 재료 목록 조회 (필터 지원) |
| `GetIngredientDetailUseCase` | 재료 상세 조회 |
| `SearchIngredientUseCase` | 재료 검색 |
| `ManageRecentSearchUseCase` | 최근 검색어 CRUD |
| `AddIngredientToListUseCase` | 재료 목록에 추가 |
| `UpdateIngredientUseCase` | 재료 수정 (즐겨찾기 토글) |
| `DeleteIngredientUseCase` | 재료 삭제 |

#### 구현된 데이터 레이어
| Class | Purpose |
|-------|---------|
| `IngredientDataSource` | 재료 데이터 소스 인터페이스 |
| `FakeIngredientDataSource` | Mock 재료 데이터 (10+ 항목) |
| `RecentSearchDataSource` | 검색어 데이터 소스 인터페이스 |
| `FakeRecentSearchDataSource` | Mock 검색어 데이터 (최대 10개) |
| `IngredientRepositoryImpl` | Repository 구현체 |
| `IngredientDataModule` | Hilt DI 모듈 |

#### 미구현 항목 (Phase 2)
- FR-004-004: 재료 추가 버튼/화면
- FR-004-008: 가격 수정 기능 (바텀시트)
- FR-004-009: 단위 변경 기능
- 더보기(⋮) 아이콘 액션

---

## 5. 대시보드 / 홈 (Dashboard / Home)

### Status
`Planned`

### Overview
앱 실행 시 처음 표시되는 화면. 전략 To-do 요약과 빠른 액션을 제공.

### User Stories
- As a 사장님, I want to 앱 실행 시 바로 할 일을 보고 싶다 so that 시간을 절약할 수 있다.
- As a 사장님, I want to 중요한 이슈를 한눈에 파악하고 싶다 so that 빠르게 대응할 수 있다.

### Functional Requirements
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-005-001 | 전략 To-do 요약 카드 (3개) | High | 최대 3개의 우선순위 높은 전략 표시 |
| FR-005-002 | To-do 상세 내용 | High | 예: "바닐라 라떼 가격 +300원 조정 시 원가율 35% 유지" |
| FR-005-003 | 원가 이상 알림 | High | 예: "우유 가격 18% 인상 감지 → 대체 브랜드 비교 권장" |
| FR-005-004 | 저마진 메뉴 알림 | Medium | 예: "레몬티 마진·판매량 저조 → 시즌 메뉴 전환 권장" |
| FR-005-005 | 저장된 전략 빠른 접근 | Medium | 북마크한 전략 바로가기 |
| FR-005-006 | 전체 메뉴로 이동 | High | 메뉴 관리 화면 네비게이션 |
| FR-005-007 | AI 전략 생성 바로가기 | High | AI 수익 전략 화면 네비게이션 |

### Non-Functional Requirements
| Category | Requirement |
|----------|-------------|
| Performance | 대시보드 로딩 2초 이내 |
| UX | 카드 형식의 직관적 UI |
| Refresh | Pull-to-refresh로 데이터 갱신 |

### Technical Considerations
- DashboardViewModel에서 여러 UseCase 조합 (GetPriorityStrategiesUseCase, GetCostAnomaliesUseCase)
- Flow combine으로 다중 데이터 소스 통합

### Open Questions
- [ ] To-do 표시 우선순위 알고리즘 정의 필요

---

## 6. AI 수익 전략 (AI Profit Strategy)

### Status
`Planned`

### Overview
AI 기반으로 메뉴별/전체 수익성을 분석하고, 실행 가능한 전략 가이드를 생성하는 핵심 기능.

### User Stories
- As a 사장님, I want to AI가 수익 개선 방법을 알려주길 원한다 so that 전문가 없이도 경영 개선을 할 수 있다.
- As a 사장님, I want to 전략을 실행하고 결과를 확인하고 싶다 so that 효과를 측정할 수 있다.

### Functional Requirements

#### 수익 현황 요약 카드
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-006-001 | 레드 플래그 메뉴 수 | High | 원가율 45% 이상 메뉴 개수 표시 |
| FR-006-002 | 이상 감지 수 | High | 원가 급등, 비정상 패턴 개수 |
| FR-006-003 | 평균 원가율 | High | 전체 메뉴 평균 원가율 |
| FR-006-004 | 총 공헌이익 예측 | Medium | 전주 대비 변화율 (예: +12%) |
| FR-006-005 | "해결하기" 버튼 | High | 전략 생성 페이지로 이동 |

#### 전략 가이드 카드
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-006-006 | AI 전략 생성 버튼 | High | 클릭 시 3개 전략 카드 동시 생성 |
| FR-006-007 | 전략 카드 구성 | High | 전략 제목, 설명, 예상 효과 표시 |
| FR-006-008 | 전략 유형 | High | 가격조정, 레시피최적화, 메뉴유지/제거, 세트최적화, 원가이상대응 |
| FR-006-009 | 예상 효과 표시 | High | "효과: 월 공헌이익 +8%" 형식 |
| FR-006-010 | 북마크 기능 | Medium | 전략 저장하기 |
| FR-006-011 | 실행 버튼 | High | 전략 실행 상태로 변경 |
| FR-006-012 | 저장된 전략 모아보기 | Medium | 북마크한 전략 리스트 |
| FR-006-013 | 전략 완료 버튼 | High | 카드 제거 및 완료 처리 |

#### 전략 템플릿
| ID | Requirement | Priority | Template |
|----|-------------|----------|----------|
| FR-006-014 | 가격 조정 전략 | High | "바닐라 라떼 원가율 42% → +300원 조정 시 35%로 개선" |
| FR-006-015 | 레시피 최적화 전략 | High | "바닐라 파우더 10% 감량 → 140원 절감, 맛 영향 미미" |
| FR-006-016 | 저마진 메뉴 제거/전환 | Medium | "레몬티 원가율 35% + 회전율 저조 → 제거 또는 시즌 메뉴 전환 권장" |
| FR-006-017 | 세트/콤보 최적화 | Medium | "아메리카노(원가율 양호) + 쿠키(마진 높음) 세트 시 마진 19% 상승" |
| FR-006-018 | 원가 이상 대응 | High | "우유 가격 상승으로 라떼류 원가율 상승 → 대체 브랜드 비교 권장" |

### Non-Functional Requirements
| Category | Requirement |
|----------|-------------|
| Performance | 전략 생성 5초 이내 (로컬), 10초 이내 (AI API) |
| Privacy | 금융 데이터는 기본 로컬 처리, 클라우드 전송 시 사용자 동의 |
| Offline | 기본 분석 및 규칙 기반 전략은 오프라인에서도 동작 |

### Technical Considerations
- **Hybrid AI Architecture** 채택:
  - **Tier 1 (Core)**: 규칙 기반 엔진 - 수학적 공식 기반 계산, 100% 오프라인
  - **Tier 2 (Enhancement)**: TensorFlow Lite - 트렌드 예측, 패턴 인식 (선택)
  - **Tier 3 (Premium)**: OpenAI API - 자연어 인사이트, 시장 컨텍스트 (선택)
- AnalyzeProfitUseCase, GenerateStrategyUseCase 분리
- Strategy Entity에 status (pending/in_progress/completed/bookmarked) 관리

### AI Implementation Details

#### 규칙 기반 엔진 (필수)
```kotlin
object FinancialFormulas {
    // 손익분기점 (단위)
    fun calculateBreakEvenUnits(fixedCosts: Double, pricePerUnit: Double, variableCostPerUnit: Double): Int
    
    // 손익분기점 (매출액)
    fun calculateBreakEvenRevenue(fixedCosts: Double, contributionMarginRatio: Double): Double
    
    // 수익률
    fun calculateProfitMargin(revenue: Double, totalCosts: Double): Double
    
    // 공헌이익
    fun calculateContributionMargin(sellingPrice: Double, variableCosts: Double): Double
}
```

#### 전략 생성 규칙
```kotlin
// 원가율 기반 전략 생성
if (costRatio > 0.42) → 가격 조정 전략 생성
if (costRatio > 0.35 && salesVolume < avgSalesVolume) → 메뉴 제거/전환 전략 생성
if (ingredientPriceChange > 0.15) → 원가 이상 대응 전략 생성
```

### Open Questions
- [ ] AI API 사용 시 과금 모델 (무료/프리미엄)
- [ ] 전략 효과 측정 방법 (실행 전후 비교)
- [ ] 전략 히스토리 보관 기간

---

## 7. 설정 (Settings)

### Status
`Planned`

### Overview
매장 정보, 고정비 설정, 구독 관리 등 앱 설정 기능.

### User Stories
- As a 사장님, I want to 매장 정보를 수정하고 싶다 so that 정확한 분석을 받을 수 있다.
- As a 사장님, I want to 고정비를 설정하고 싶다 so that 더 정확한 손익분기점을 알 수 있다.

### Functional Requirements
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-007-001 | 매장 정보 수정 | High | 매장명, 업종, 영업시간 |
| FR-007-002 | 고정비 설정 | Medium | 월세, 인건비, 공과금, 감가상각비 등 입력 |
| FR-007-003 | 목표 원가율 설정 | Medium | 기본 30%, 사용자 조정 가능 |
| FR-007-004 | 데이터 백업/복원 | Medium | 로컬 데이터 내보내기/가져오기 |
| FR-007-005 | 구독 관리 | Low | (Future) 프리미엄 기능 구독 |
| FR-007-006 | 알림 설정 | Low | 원가 이상 알림, 전략 리마인더 |
| FR-007-007 | 앱 정보 | Low | 버전, 개인정보처리방침, 이용약관 |

### Non-Functional Requirements
| Category | Requirement |
|----------|-------------|
| 데이터 | 설정 변경 즉시 반영 |
| 백업 | JSON 형식 내보내기 지원 |

### Technical Considerations
- DataStore Preferences로 설정값 관리
- 고정비는 FixedCost Entity로 Room에 저장

### Open Questions
- [ ] 클라우드 백업 지원 여부 (Google Drive 연동)

---

## 8. 데이터 모델 (Data Models)

### Status
`Planned`

### Entity Relationship Diagram

```
┌─────────────┐       ┌─────────────────┐       ┌─────────────┐
│    Store    │       │  MenuIngredient │       │ Ingredient  │
├─────────────┤       ├─────────────────┤       ├─────────────┤
│ id (PK)     │       │ menuId (FK)     │       │ id (PK)     │
│ name        │       │ ingredientId(FK)│       │ name        │
│ businessType│       │ quantity        │       │ unit        │
│ createdAt   │       │ unit            │       │ unitPrice   │
│ updatedAt   │       └─────────────────┘       │ category    │
└─────────────┘               │                 │ supplier    │
      │                       │                 │ isDeleted   │
      │                       ▼                 │ createdAt   │
      │               ┌─────────────┐           │ updatedAt   │
      │               │    Menu     │           └─────────────┘
      │               ├─────────────┤                 │
      │               │ id (PK)     │                 │
      └──────────────▶│ storeId(FK) │                 │
                      │ name        │           ┌─────────────┐
                      │ sellingPrice│           │ PriceHistory│
                      │ category    │           ├─────────────┤
                      │ prepTime    │◀──────────│ingredientId │
                      │ createdAt   │           │ price       │
                      │ updatedAt   │           │ recordedAt  │
                      └─────────────┘           └─────────────┘
                            │
                            │
                      ┌─────────────┐
                      │  Strategy   │
                      ├─────────────┤
                      │ id (PK)     │
                      │ menuId (FK) │ (nullable - 전체 전략일 수 있음)
                      │ type        │
                      │ title       │
                      │ description │
                      │ expectedEffect │
                      │ status      │
                      │ isBookmarked│
                      │ createdAt   │
                      │ completedAt │
                      └─────────────┘

┌─────────────┐
│  FixedCost  │
├─────────────┤
│ id (PK)     │
│ storeId(FK) │
│ type        │ (rent, labor, utilities, depreciation, other)
│ name        │
│ amount      │
│ isMonthly   │
│ createdAt   │
└─────────────┘
```

### Entity Definitions

#### Store (매장)
```kotlin
@Entity(tableName = "stores")
data class Store(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val businessType: BusinessType,
    val targetCostRatio: Double = 0.30,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class BusinessType {
    CAFE, RESTAURANT, BAKERY, BAR, RETAIL, OTHER
}
```

#### Menu (메뉴)
```kotlin
@Entity(tableName = "menus")
data class Menu(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storeId: Long,
    val name: String,
    val sellingPrice: Double,
    val category: MenuCategory,
    val prepTimeMinutes: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class MenuCategory {
    BEVERAGE, DESSERT, FOOD, SET, OTHER
}
```

#### Ingredient (재료)
```kotlin
@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val unit: IngredientUnit,
    val unitPrice: Double,
    val category: IngredientCategory,
    val supplier: String? = null,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class IngredientUnit {
    GRAM, MILLILITER, PIECE
}

enum class IngredientCategory {
    RAW_MATERIAL,    // 원재료 (커피, 우유 등)
    PACKAGING,       // 포장재 (컵, 뚜껑 등)
    CONSUMABLE,      // 소모품 (빨대, 냅킨 등)
    OTHER
}
```

#### MenuIngredient (메뉴-재료 관계)
```kotlin
@Entity(
    tableName = "menu_ingredients",
    primaryKeys = ["menuId", "ingredientId"]
)
data class MenuIngredient(
    val menuId: Long,
    val ingredientId: Long,
    val quantity: Double,
    val unit: IngredientUnit
)
```

#### Strategy (전략)
```kotlin
@Entity(tableName = "strategies")
data class Strategy(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val menuId: Long? = null,  // null이면 전체 매장 전략
    val type: StrategyType,
    val title: String,
    val description: String,
    val expectedEffect: String,
    val status: StrategyStatus = StrategyStatus.PENDING,
    val isBookmarked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

enum class StrategyType {
    PRICE_ADJUSTMENT,      // 가격 조정
    RECIPE_OPTIMIZATION,   // 레시피 최적화
    MENU_REMOVAL,          // 메뉴 제거/전환
    SET_OPTIMIZATION,      // 세트 최적화
    COST_ANOMALY_RESPONSE  // 원가 이상 대응
}

enum class StrategyStatus {
    PENDING,      // 대기
    IN_PROGRESS,  // 실행 중
    COMPLETED,    // 완료
    DISMISSED     // 무시됨
}
```

#### FixedCost (고정비)
```kotlin
@Entity(tableName = "fixed_costs")
data class FixedCost(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storeId: Long,
    val type: FixedCostType,
    val name: String,
    val amount: Double,
    val isMonthly: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

enum class FixedCostType {
    RENT,          // 월세
    LABOR,         // 인건비
    UTILITIES,     // 공과금
    DEPRECIATION,  // 감가상각비
    OTHER          // 기타
}
```

#### PriceHistory (가격 이력)
```kotlin
@Entity(tableName = "price_history")
data class PriceHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ingredientId: Long,
    val price: Double,
    val recordedAt: Long = System.currentTimeMillis()
)
```

---

## 9. 기술 스택 및 아키텍처

### Status
`Planned`

### Technology Stack

| Category | Technology | Version |
|----------|------------|---------|
| Language | Kotlin | 2.0.21 |
| UI | Jetpack Compose | BOM 2024.09.00 |
| Architecture | Clean Architecture + MVVM | - |
| DI | Hilt | 2.56.1 |
| Navigation | Compose Navigation | 2.9.0 |
| Local DB | Room | (추가 필요) |
| Preferences | DataStore | (추가 필요) |
| Async | Kotlin Coroutines + Flow | 1.10.2 |
| AI (Optional) | TensorFlow Lite | 2.14.0 |
| Network (Optional) | OkHttp + Retrofit | 4.12.0 |
| Serialization | Kotlinx Serialization | 1.6.2 |

### Module Structure (확장)

```
app/                          # Application layer
├── feature/
│   ├── feature-home/         # 기존 홈 → 대시보드로 전환
│   ├── feature-onboarding/   # 온보딩 플로우
│   ├── feature-menu/         # 메뉴 관리
│   ├── feature-ingredient/   # 재료 관리
│   ├── feature-strategy/     # AI 수익 전략
│   └── feature-settings/     # 설정
└── core/
    ├── core-common/          # 공통 유틸리티
    ├── core-data/            # Repository 구현, DataSource
    ├── core-database/        # Room Database, DAO, Entity
    ├── core-domain/          # UseCase, Repository Interface, Domain Model
    ├── core-ui/              # 공통 UI 컴포넌트
    └── core-ai/              # AI/ML 관련 (선택)
```

### Dependency Direction

```
feature-* → core-domain ← core-data ← core-database
    ↓           ↓            ↓
  core-ui   core-common  core-common
                              ↑
                           core-ai (optional)
```

---

## 10. 우선순위 및 개발 Phase

### Status
`Planned`

### Phase 1: MVP (4주)

**목표**: 핵심 기능으로 앱 출시 가능한 상태

| 주차 | Feature | 세부 내용 |
|------|---------|-----------|
| 1주차 | 프로젝트 설정 | Room 추가, core-database 모듈, Entity/DAO 구현 |
| 1주차 | 온보딩 | 기본 플로우, 매장/메뉴 1개 등록 |
| 2주차 | 재료 관리 | 재료 CRUD, 카테고리 필터 |
| 2주차 | 메뉴 관리 | 메뉴 CRUD, 재료 연결 |
| 3주차 | 원가 분석 | 원가 계산 로직, 분석 카드 UI |
| 3주차 | 대시보드 | 요약 카드, 네비게이션 |
| 4주차 | 규칙 기반 전략 | 기본 전략 생성, 전략 카드 UI |
| 4주차 | 테스트 및 버그 수정 | UI 테스트, 계산 로직 검증 |

**MVP 범위**:
- ✅ 온보딩 (FR-002-001~008)
- ✅ 메뉴 CRUD (FR-003-001~010)
- ✅ 원가 분석 (FR-003-011~015)
- ✅ 재료 CRUD (FR-004-001~015)
- ✅ 대시보드 기본 (FR-005-001~007)
- ✅ 규칙 기반 전략 (FR-006-001~018 중 Tier 1)
- ✅ 설정 기본 (FR-007-001~003)

### Phase 2: Enhancement (2주)

**목표**: UX 개선 및 고급 기능

| Feature | 세부 내용 |
|---------|-----------|
| 가격 이력 | 재료 가격 변동 추적, 그래프 |
| 전략 관리 | 북마크, 완료 처리, 히스토리 |
| 고정비 관리 | 상세 고정비 입력, 손익분기점 계산 |
| 데이터 백업 | JSON 내보내기/가져오기 |
| UI 개선 | 애니메이션, 인터랙션 개선 |

### Phase 3: AI Integration (2주)

**목표**: AI 기반 고급 분석

| Feature | 세부 내용 |
|---------|-----------|
| TensorFlow Lite | 트렌드 예측 모델 통합 |
| OpenAI API | 자연어 전략 추천 (선택) |
| 이상 탐지 | 원가 급등 자동 감지 |

### Phase 4: Growth (지속)

**목표**: 확장 기능

| Feature | 세부 내용 |
|---------|-----------|
| 클라우드 동기화 | 멀티 디바이스 지원 |
| 프리미엄 구독 | AI 기능 과금 모델 |
| 알림 | 푸시 알림, 리마인더 |
| 리포트 | 월간/분기 리포트 생성 |

---

## 11. 비기능 요구사항 종합

### Status
`Planned`

### Performance
| Metric | Requirement |
|--------|-------------|
| 앱 시작 시간 | Cold start 3초 이내 |
| 화면 전환 | 500ms 이내 |
| 원가 계산 | 100개 메뉴 기준 1초 이내 |
| AI 전략 생성 | 로컬 5초, 클라우드 10초 이내 |
| 메모리 사용량 | 150MB 이내 |

### Security & Privacy
| Requirement | Description |
|-------------|-------------|
| 데이터 저장 | 기본 로컬 저장 (Room + Encrypted SharedPreferences) |
| 클라우드 전송 | 사용자 명시적 동의 필수, TLS 암호화 |
| 금융 데이터 | 기기 외부 전송 시 익명화 또는 암호화 |
| 인증 | (Phase 4) 생체 인증 또는 PIN 옵션 |

### Accessibility
| Requirement | Description |
|-------------|-------------|
| 최소 터치 영역 | 48dp × 48dp |
| 색상 대비 | WCAG 2.1 AA 기준 충족 |
| 스크린 리더 | TalkBack 지원 |
| 텍스트 크기 | 시스템 폰트 크기 연동 |

### Offline Support
| Feature | Offline Capability |
|---------|-------------------|
| 메뉴/재료 CRUD | ✅ 완전 지원 |
| 원가 분석 | ✅ 완전 지원 |
| 규칙 기반 전략 | ✅ 완전 지원 |
| AI 전략 (TFLite) | ✅ 완전 지원 |
| AI 전략 (OpenAI) | ❌ 인터넷 필요 |

### Compatibility
| Requirement | Value |
|-------------|-------|
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |
| 지원 언어 | 한국어 (영어 - Phase 4) |

---

## 12. 회원가입/로그인 (Authentication)

### Status
`In Progress`

### Overview
사용자가 아이디와 비밀번호로 회원가입 및 로그인할 수 있는 인증 기능. 온보딩 완료 후 회원가입 화면으로 이동하며, 이후 앱 접속 시 자동 로그인 상태를 유지.

### User Stories
| ID | User Story | Priority |
|----|------------|----------|
| US-012-001 | As a 신규 사용자, I want to 아이디와 비밀번호로 회원가입하고 싶다 so that 앱을 사용할 수 있다 | High |
| US-012-002 | As a 기존 사용자, I want to 로그인하고 싶다 so that 내 데이터에 접근할 수 있다 | High |
| US-012-003 | As a 로그인한 사용자, I want to 앱을 다시 열었을 때 자동으로 로그인되고 싶다 so that 매번 인증하지 않아도 된다 | High |
| US-012-004 | As a 사용자, I want to 로그아웃하고 싶다 so that 다른 계정으로 전환할 수 있다 | Medium |

### Functional Requirements

#### 로그인 (Sign In)
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-012-001 | 아이디 입력 필드 | High | 입력 필드 제공, 빈 값 검증 |
| FR-012-002 | 비밀번호 입력 필드 | High | 입력 필드 제공, 마스킹 처리 |
| FR-012-003 | 비밀번호 표시/숨김 토글 | Medium | 눈 아이콘으로 토글 가능 |
| FR-012-004 | 로그인 버튼 | High | 클릭 시 인증 처리, 실패 시 오류 메시지 |
| FR-012-005 | 로그인 실패 오류 메시지 | High | 잘못된 아이디/비밀번호 시 명확한 안내 |
| FR-012-006 | 회원가입 화면 이동 링크 | High | "계정이 없으신가요? 회원가입" 클릭 시 회원가입 화면으로 이동 |
| FR-012-007 | 자동 로그인 유지 | High | 앱 재실행 시 토큰 기반 자동 인증 |

#### 회원가입 (Sign Up)
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-012-008 | 아이디 입력 필드 | High | 최소 4자 이상, 영문+숫자 조합 검증 |
| FR-012-009 | 비밀번호 입력 필드 | High | 최소 8자, 영문+숫자+특수문자 조합 검증 |
| FR-012-010 | 비밀번호 확인 입력 필드 | High | 비밀번호와 일치 여부 검증 |
| FR-012-011 | 실시간 유효성 검사 표시 | High | 입력 중 조건 충족 여부 시각적 피드백 |
| FR-012-012 | 아이디 중복 확인 | High | 이미 가입된 아이디인 경우 오류 메시지 |
| FR-012-013 | 이용약관 동의 체크박스 | Medium | 체크하지 않으면 회원가입 버튼 비활성화 |
| FR-012-014 | 회원가입 버튼 | High | 모든 조건 충족 시 활성화, 클릭 시 계정 생성 |
| FR-012-015 | 회원가입 성공 시 자동 로그인 | High | 홈 화면으로 이동 |
| FR-012-016 | 로그인 화면 이동 링크 | High | "이미 계정이 있으신가요? 로그인" 클릭 시 로그인 화면으로 이동 |

#### 로그아웃
| ID | Requirement | Priority | Acceptance Criteria |
|----|-------------|----------|---------------------|
| FR-012-017 | 로그아웃 버튼 (설정 화면) | Medium | 클릭 시 확인 다이얼로그 표시 |
| FR-012-018 | 토큰 삭제 | Medium | 로컬 저장된 인증 토큰 완전 삭제 |
| FR-012-019 | 로그인 화면 이동 | Medium | 로그아웃 후 로그인 화면으로 네비게이션 |

### Non-Functional Requirements

#### Security
| Category | Requirement |
|----------|-------------|
| 비밀번호 해싱 | 서버에서 bcrypt/Argon2 사용 (클라이언트 평문 전송, HTTPS 필수) |
| 토큰 저장 | EncryptedSharedPreferences 사용 (AndroidKeyStore 기반 암호화) |
| 토큰 관리 | Access Token (단기) + Refresh Token (장기) 패턴 |
| 입력 검증 | 클라이언트 + 서버 양쪽에서 검증 |

#### UX
| Requirement | Description |
|-------------|-------------|
| 키보드 처리 | 아이디 → 비밀번호 → Done 키보드 액션 체인 |
| 로딩 상태 | 버튼에 로딩 인디케이터 표시 |
| 에러 처리 | 인라인 에러 메시지 (필드 하단) |

### Technical Specifications

#### Architecture
```
feature/
├── feature-auth/                    # 새 인증 모듈
│   ├── src/main/kotlin/.../auth/
│   │   ├── navigation/
│   │   │   └── AuthNavigation.kt
│   │   ├── login/
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   └── LoginUiState.kt
│   │   ├── signup/
│   │   │   ├── SignUpScreen.kt
│   │   │   ├── SignUpViewModel.kt
│   │   │   └── SignUpUiState.kt
│   │   └── component/
│   │       └── AuthTextField.kt
│   └── build.gradle.kts

core/
├── core-domain/
│   ├── model/
│   │   ├── User.kt
│   │   └── AuthToken.kt
│   └── repository/
│       └── AuthRepository.kt

├── core-data/
│   ├── repository/
│   │   └── AuthRepositoryImpl.kt
│   ├── datasource/
│   │   ├── AuthRemoteDataSource.kt
│   │   └── AuthLocalDataSource.kt
│   └── di/
│       └── AuthModule.kt
```

#### Domain Models
```kotlin
// core-domain/model/User.kt
data class User(
    val id: String,
    val username: String,
    val createdAt: Long
)

// core-domain/model/AuthToken.kt
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
)

// core-domain/model/AuthResult.kt
sealed interface AuthResult {
    data class Success(val user: User, val token: AuthToken) : AuthResult
    data class InvalidCredentials(val message: String) : AuthResult
    data class UsernameAlreadyExists(val message: String) : AuthResult
    data class NetworkError(val exception: Throwable) : AuthResult
    data class ValidationError(val errors: Map<String, String>) : AuthResult
}
```

#### Repository Interface
```kotlin
// core-domain/repository/AuthRepository.kt
interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>
    suspend fun signIn(username: String, password: String): AuthResult
    suspend fun signUp(username: String, password: String): AuthResult
    suspend fun signOut()
    suspend fun refreshToken(): Result<AuthToken>
    suspend fun checkUsernameAvailability(username: String): Boolean
}

sealed interface AuthState {
    data object Loading : AuthState
    data class Authenticated(val user: User) : AuthState
    data object Unauthenticated : AuthState
}
```

#### Validation Rules
```kotlin
object AuthValidationRules {
    // 아이디 검증
    const val MIN_USERNAME_LENGTH = 4
    val USERNAME_PATTERN = Regex("^[A-Za-z0-9]+$")
    
    // 비밀번호 검증
    const val MIN_PASSWORD_LENGTH = 8
    val PASSWORD_HAS_LETTER = Regex("[A-Za-z]")
    val PASSWORD_HAS_DIGIT = Regex("\\d")
    val PASSWORD_HAS_SPECIAL = Regex("[!@#\$%^&*(),.?\":{}|<>]")
    
    fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "아이디를 입력해주세요"
            username.length < MIN_USERNAME_LENGTH -> "아이디는 4자 이상이어야 합니다"
            !USERNAME_PATTERN.matches(username) -> "영문과 숫자만 사용 가능합니다"
            else -> null
        }
    }
    
    fun validatePassword(password: String): String? {
        return when {
            password.length < MIN_PASSWORD_LENGTH -> "비밀번호는 8자 이상이어야 합니다"
            !PASSWORD_HAS_LETTER.containsMatchIn(password) -> "영문을 포함해야 합니다"
            !PASSWORD_HAS_DIGIT.containsMatchIn(password) -> "숫자를 포함해야 합니다"
            !PASSWORD_HAS_SPECIAL.containsMatchIn(password) -> "특수문자를 포함해야 합니다"
            else -> null
        }
    }
}
```

#### Navigation Flow
```
온보딩 완료 → 회원가입 화면 → (가입 성공) → 홈 화면
                ↓
        로그인 화면 ← → 회원가입 화면
                ↓
        (로그인 성공) → 홈 화면

앱 재실행 (토큰 있음) → 홈 화면
앱 재실행 (토큰 없음) → 로그인 화면
```

### UI Mockups

#### Login Screen
```
┌─────────────────────────────────┐
│                                 │
│         [앱 로고/이름]           │
│                                 │
│  ┌─────────────────────────┐    │
│  │ 아이디                   │    │
│  └─────────────────────────┘    │
│                                 │
│  ┌─────────────────────────┐    │
│  │ 비밀번호           [👁]  │    │
│  └─────────────────────────┘    │
│                                 │
│  ┌─────────────────────────┐    │
│  │       로그인            │    │
│  └─────────────────────────┘    │
│                                 │
│     계정이 없으신가요? 회원가입   │
│                                 │
└─────────────────────────────────┘
```

#### Sign Up Screen
```
┌─────────────────────────────────┐
│  ← 뒤로                         │
│                                 │
│         회원가입                 │
│                                 │
│  ┌─────────────────────────┐    │
│  │ 아이디                   │    │
│  └─────────────────────────┘    │
│  ✓ 사용 가능한 아이디입니다       │
│                                 │
│  ┌─────────────────────────┐    │
│  │ 비밀번호           [👁]  │    │
│  └─────────────────────────┘    │
│  ✓ 영문 ✓ 숫자 ✓ 특수문자 ✓ 8자+ │
│                                 │
│  ┌─────────────────────────┐    │
│  │ 비밀번호 확인       [👁]  │    │
│  └─────────────────────────┘    │
│                                 │
│  ☑ 이용약관에 동의합니다          │
│                                 │
│  ┌─────────────────────────┐    │
│  │       회원가입           │    │
│  └─────────────────────────┘    │
│                                 │
│     이미 계정이 있으신가요? 로그인 │
│                                 │
└─────────────────────────────────┘
```

### Dependencies
```toml
# gradle/libs.versions.toml
[versions]
security-crypto = "1.1.0-alpha06"

[libraries]
androidx-security-crypto = { group = "androidx.security", name = "security-crypto", version.ref = "security-crypto" }
```

### Phase 1 (MVP) 범위
- ✅ 로그인 화면 (FR-012-001~007) - **구현 완료**
- ✅ 회원가입 화면 (FR-012-008~016) - **구현 완료** (현재 미사용)
- ⏳ 로그아웃 (FR-012-017~019) - 미구현
- ✅ 로컬 인증 (서버 없이 DataStore 기반 mock 구현) - **구현 완료**
- ✅ 자동 로그인 유지 - **구현 완료**

### Implementation Details (2026-01-02)

#### 구현된 네비게이션 플로우
```
온보딩 완료 → 로그인 화면 (fr012_01)
    ↓
"처음이신가요?" 클릭 → 매장정보 입력 (fr012_02) → 메뉴관리 (fr012_04/05) ↔ 메뉴입력 (fr012_03)
    ↓                                                    ↓
로그인 성공 → 홈                               준비완료 (fr012_06) → 홈
```

#### 구현된 모듈
- **feature-auth**: 로그인/회원가입 UI 및 ViewModel
- **feature-setup**: 초기 설정 플로우 (매장정보, 메뉴입력, 메뉴관리, 완료화면)
- **core-domain**: AuthRepository 인터페이스, User/AuthToken/AuthResult 모델
- **core-data**: AuthRepositoryImpl (DataStore 기반 Mock)

### Phase 2 범위
- 실제 백엔드 API 연동
- 비밀번호 찾기/재설정
- 소셜 로그인 (Google, Kakao)
- 생체 인증 (지문/Face)

### Open Questions
- [ ] 백엔드 API 스펙 확정 필요
- [ ] 세션 만료 시간 (Access Token 유효 기간)
- [ ] 다중 기기 로그인 허용 여부

---

## 13. 초기 설정 - 매장 정보 (Store Info Setup)

### Status
`Done`

### Overview
신규 사용자가 "처음이신가요?" 클릭 후 매장 정보를 입력하는 초기 설정 플로우. 매장명, 위치, 직원수를 단계별로 입력.

### User Stories
| ID | User Story | Priority |
|----|------------|----------|
| US-013-001 | As a 신규 사용자, I want to 매장명을 입력하고 싶다 so that 내 매장을 등록할 수 있다 | High |
| US-013-002 | As a 신규 사용자, I want to 매장 위치를 검색하고 싶다 so that 정확한 위치를 등록할 수 있다 | High |
| US-013-003 | As a 신규 사용자, I want to 직원수를 입력하고 싶다 so that 인건비 계산에 활용할 수 있다 | High |

### Functional Requirements
| ID | Requirement | Priority | Status | Acceptance Criteria |
|----|-------------|----------|--------|---------------------|
| FR-013-001 | 매장명 입력 화면 | High | ✅ Done | 언더라인 텍스트필드, 포커스 시 라벨 색상 변경 |
| FR-013-002 | 매장 위치 검색 화면 | High | ✅ Done | 검색 아이콘 포함 언더라인 필드 |
| FR-013-003 | 확인 화면 | High | ✅ Done | 입력된 정보 확인 후 "확인" 버튼 |
| FR-013-004 | 직원수 입력 바텀시트 | High | ✅ Done | +/- 카운터 버튼, 툴팁 도움말 |
| FR-013-005 | 설정 완료 화면 | High | ✅ Done | 체크 아이콘, 입력 정보 요약 카드 |

### Navigation Flow
매장명 입력 → 매장 위치 검색 → 확인 → 직원수 바텀시트 → 메뉴 등록 온보딩

---

## 14. 초기 설정 - 메뉴 등록 온보딩 (Menu Registration Onboarding)

### Status
`In Progress`

### Overview
매장 정보 입력 후 메뉴를 등록하는 온보딩 플로우. 메뉴 템플릿 검색, 상세 정보 입력, 재료 추가를 통해 최소 1개 이상의 메뉴를 등록.

### User Stories
| ID | User Story | Priority |
|----|------------|----------|
| US-014-001 | As a 신규 사용자, I want to 메뉴 템플릿을 검색하고 싶다 so that 빠르게 메뉴를 등록할 수 있다 | High |
| US-014-002 | As a 신규 사용자, I want to 메뉴 가격과 카테고리를 입력하고 싶다 so that 원가 분석에 활용할 수 있다 | High |
| US-014-003 | As a 신규 사용자, I want to 재료를 추가하고 싶다 so that 정확한 원가를 계산할 수 있다 | High |

### Functional Requirements
| ID | Requirement | Priority | Status | Acceptance Criteria |
|----|-------------|----------|--------|---------------------|
| FR-014-001 | 메뉴 등록 시작 화면 | High | ✅ Done | 계산기 아이콘, "메뉴 등록 시작하기" 버튼 |
| FR-014-002 | 메뉴 템플릿 검색 화면 | High | ✅ Done | 검색 자동완성, 템플릿 적용 다이얼로그 |
| FR-014-003 | 메뉴 상세 입력 화면 | High | ✅ Done | 메뉴명, 가격(천단위 포맷), 카테고리, 제조시간 |
| FR-014-004 | 재료 입력 화면 | High | ✅ Done | 재료 추가 바텀시트, 재료 목록 표시 |
| FR-014-005 | 메뉴 등록 확인 화면 | High | ✅ Done | 등록 완료 메시지, 추가 등록/마치기 버튼 |

### Navigation Flow
MenuSuggestion → MenuSearch → MenuDetail → IngredientInput → MenuConfirm → (추가 등록: MenuSearch / 마치기: SetupComplete)

### Implementation Details (2026-01-20)

#### 구현된 화면
1. **MenuSuggestionScreen** - FR-014-001 완료
   - 계산기 3D 아이콘 (ic_3d_calculator)
   - "메뉴 등록 시작하기" ChordLargeButton

2. **MenuSearchScreen** - FR-014-002 완료
   - ChordSearchBar 사용 (입력 시 툴팁 숨김)
   - 검색 결과 자동완성 (FakeMenuDataSource)
   - TemplateApplyDialog (ModalBottomSheet) - 템플릿 적용 확인

3. **MenuDetailScreen** - FR-014-003 완료
   - StepIndicator (1/2 단계 표시)
   - 메뉴명 입력 (BasicTextField + 삭제 버튼)
   - 가격 입력 (천단위 콤마 포맷팅, "원" 접미사)
   - 카테고리 라디오 버튼 (offset으로 정렬 보정)
   - 제조시간 Row (ic_chevron_right, 툴팁 3초 자동 숨김)
   - 템플릿 적용 시 Snackbar 토스트 (SnackbarHostState)
   - ChordLargeButton (이전/다음 버튼, 색상 커스터마이징)

4. **IngredientInputScreen** - FR-014-004 완료
   - 재료 추가 바텀시트
   - 재료 목록 표시

5. **MenuConfirmScreen** - FR-014-005 완료
   - 등록 완료 메시지
   - "추가 등록" → MenuSearchScreen
   - "마치기" → SetupCompleteScreen

#### 구현된 컴포넌트
| Component | Purpose |
|-----------|---------|
| `StepIndicator` | 단계 표시 (1-2 진행률, Grayscale400 비활성) |
| `TemplateApplyDialog` | 템플릿 적용 바텀시트 (lineHeight 160%) |
| `PriceTextField` | 가격 입력 필드 (천단위 포맷팅, "원" 접미사) |
| `CategoryRadioGroup` | 카테고리 선택 (offset -12dp 정렬) |
| `PreparationTimeRow` | 제조시간 설정 (툴팁 3초 자동 숨김) |
| `BottomNavigationButtons` | 이전/다음 버튼 (ChordLargeButton 사용) |

#### 추가된 core-ui 컴포넌트
| Component | Purpose |
|-----------|---------|
| `ChordToast` | 재사용 가능한 토스트 (배경 #151B26B2, leadingIcon 원형) |
| `ChordLargeButton` | backgroundColor, textColor 파라미터 추가 |

---

*Last Updated: 2026-01-20 (FR-014 메뉴 등록 온보딩 구현 - MenuSuggestionScreen, MenuSearchScreen, MenuDetailScreen, IngredientInputScreen, MenuConfirmScreen)*
