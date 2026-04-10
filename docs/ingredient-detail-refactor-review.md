# Ingredient Detail Refactor Review Notes

## 목적
재료 상세 Figma parity 리팩터 작업을 진행할 때, 현재 브라운필드 구현의 구조/리스크를 빠르게 파악하고 PRD + 테스트 스펙의 결정사항을 실행 가능한 리뷰 메모로 남긴다.

## 기준 문서
- `/Users/keemhyun/side_projects/Chord_Android/.omx/plans/prd-ingredient-detail-refactor-plan.md`
- `/Users/keemhyun/side_projects/Chord_Android/.omx/plans/test-spec-ingredient-detail-refactor-plan.md`

## 현재 구현 베이스라인
### 주요 터치포인트
- `feature/feature-ingredient/.../detail/IngredientDetailScreen.kt`
- `feature/feature-ingredient/.../detail/IngredientDetailViewModel.kt`
- `feature/feature-ingredient/.../detail/IngredientDetailUiState.kt`
- `feature/feature-ingredient/.../component/UsedMenuCard.kt`
- `feature/feature-ingredient/.../component/PriceHistoryItem.kt`
- `feature/feature-ingredient/.../component/IngredientEditBottomSheet.kt`
- `feature/feature-ingredient/.../component/SupplierEditBottomSheet.kt`
- `feature/feature-ingredient/.../navigation/IngredientNavigation.kt`

### 구현 상태 요약
1. **화면 상태 분기**
   - `IngredientDetailScreenContent` 내부에서 `Loading / Error / Success` 분기를 모두 처리한다.
   - 삭제/수정/공급업체 바텀시트 가시성 상태가 모두 화면 로컬 state에 머물러 있다.
2. **헤더 / 뒤로가기 / 즐겨찾기**
   - 상단 헤더는 이미 `뒤로가기 + 즐겨찾기` affordance를 제공한다.
   - 뒤로가기 refresh 동작은 `viewModel.hasChanges()` → `IngredientNavigation`의 `INGREDIENT_LIST_REFRESH_REQUEST_KEY` 경로로 연결된다.
3. **정보 카드 / 가격 / 공급업체**
   - 현재 정보 카드는 카테고리 chip + 이름 + 가격 row + 공급업체 row를 한 카드 안에 포함한다.
   - 가격 수정 entry는 정보 카드 안에서 아이콘으로 처리되고 있다.
4. **사용 메뉴 섹션**
   - 현재 구현은 `FlowRow` + `UsedMenuCard` 조합이다.
   - 카드 폭, 라인 수, overflow 규칙이 고정되지 않아 Figma의 가로 스트립 구조와 직접적으로 맞지 않는다.
5. **가격 이력 섹션**
   - `PriceHistoryItem`은 현재도 재사용 가능한 독립 컴포넌트다.
   - 타임라인 점/선 스타일은 로컬 refactor로 정리 가능하며, 데이터 shape 변경 없이도 조정할 여지가 크다.
6. **모달 ownership**
   - `SupplierEditBottomSheet`는 공급업체만 수정한다.
   - `IngredientEditBottomSheet`는 가격/용량/단위/카테고리뿐 아니라 `supplier`까지 함께 수정한다.
   - 즉, 현재는 공급업체 수정 책임이 `공급업체 전용 모달`과 `재료 수정 모달`에 중복되어 있다.
7. **ViewModel 경계**
   - `onUpdateIngredientDetail(...)`은 supplier까지 함께 갱신한다.
   - `onUpdatePriceInfo(...)`가 이미 존재하므로, 재료 수정 모달을 가격/용량 중심으로 축소해야 할 경우 더 좁은 이벤트 경계를 활용할 수 있다.

## PRD 기준 리뷰 결과
| Slice | 현재 상태 | 리뷰 판단 | 실행 메모 |
|---|---|---|---|
| Header / back / favorite | 기능은 이미 존재 | **유지 + 스타일 보정 후보** | 레이아웃/spacing/icon emphasis만 Figma 기준으로 재조정하면 된다. |
| Info card | 정보는 모두 존재 | **구조 유지, 시각 계층 재정렬 필요** | 현재 category chip / name / price typography는 Figma target과 차이가 크다. |
| Price edit affordance | edit icon 존재 | **유지 가능, 표현 조정 필요** | CTA hierarchy는 유지 가능하지만 inline affordance 스타일은 정리 필요. |
| Supplier row | 독립 click target 존재 | **유지 가능** | 전용 supplier modal entry는 이미 분리돼 있어 Figma 방향과 잘 맞는다. |
| Used menu presentation | `FlowRow` 기반 | **변경 필요** | Figma target의 가로 카드 스트립과 구조가 달라서 `LazyRow` 계열이 더 적합하다. |
| Price history presentation | 독립 컴포넌트 존재 | **국소 refactor 적합** | 시각 계층/간격/marker 스타일을 컴포넌트 수준에서 다듬는 편이 안전하다. |
| Supplier modal | 전용 modal 이미 존재 | **compare-first** | wrapper 수준에서 충분히 해결될 가능성이 높고, shared core-ui 변경은 후순위다. |
| Ingredient edit modal | supplier field 포함 | **ownership mismatch 가능성 큼** | Figma compare 결과가 supplier 전용 흐름을 지지하면 price/category/amount-only로 축소하는 쪽이 자연스럽다. |
| Navigation refresh | detail → list refresh 경로 존재 | **유지 우선** | UI refactor만으로 유지 가능하면 navigation/state 경계는 건드리지 않는 편이 안전하다. |

## 구현 가드레일
### 1. UI 우선, 상태 확장은 증거가 있을 때만
다음 중 하나가 발생할 때만 ViewModel / navigation 변경을 정당화한다.
- UI-only 수정으로 Figma interaction semantics를 유지할 수 없음
- supplier ownership 중복 때문에 update path가 이중화됨
- 화면 분리 이후 back refresh ownership이 모호해짐
- 현재 state shape로는 필요한 parity를 표현하기 어려움

### 2. 모달 변경 우선순위
1. 이미 match-enough면 유지
2. mismatch가 있으면 feature-level wrapper (`IngredientEditBottomSheet`, `SupplierEditBottomSheet`) 먼저 수정
3. wrapper로 해결되지 않을 때만 shared `core-ui` primitive 수정

### 3. match-enough 체크 기준
모달은 아래 다섯 조건을 모두 만족할 때만 “비교 후 유지”로 분류한다.
1. 사용자 과업이 동일하다.
2. 수정 가능한 필드 집합이 동일하다.
3. CTA hierarchy가 동일하다.
4. interaction ownership이 중복되지 않는다.
5. 입력 affordance / 확인 패턴이 동일하다.

현재 코드 기준으로는 `IngredientEditBottomSheet`가 **4번 ownership 기준**에서 가장 먼저 탈락할 가능성이 높다.

## 회귀 확인 포인트
최종 구현 검수 시 최소한 아래 항목은 별도 evidence를 남긴다.
- 즐겨찾기 토글 성공 여부
- 가격/용량 수정 후 toast 노출 여부
- 공급업체 수정 후 toast 노출 여부
- 삭제 후 뒤로가기 + 목록 refresh 여부
- 사용 메뉴가 비어 있을 때 섹션 처리
- 가격 이력이 비어 있거나 1건뿐일 때 타임라인 처리
- 긴 재료명 / 긴 메뉴명에서 줄바꿈 또는 truncation 규칙

## 코드 리뷰에서 바로 보이는 주의점
- `editSupplier`가 재료 수정 sheet와 공급업체 전용 sheet에서 함께 재사용된다. UI ownership을 분리하더라도 로컬 state가 그대로 남으면 다시 중복 책임이 생길 수 있다.
- 사용 메뉴/가격 이력 섹션은 현재 “데이터가 없을 때의 표현”이 명시돼 있지 않다. Figma parity 작업 중 empty-state를 숨길지, 안내 문구를 둘지 명시해야 QA가 흔들리지 않는다.
- 공급업체 값이 빈 문자열일 때 현재 정보 카드 우측 영역이 사실상 빈 텍스트가 된다. placeholder 정책을 정하지 않으면 화면이 깨진 것처럼 보일 수 있다.
- `onUpdatePriceInfo(...)`는 이미 존재하지만 현재 화면에서 직접 사용되지 않는다. 재료 수정 modal을 좁힐 때 이 경계를 활용하면 ViewModel diff를 줄일 수 있다.

## 문서화 권장 방식
구현 완료 후에는 이 파일을 기준으로 아래 세 가지를 final evidence에 함께 남기는 것이 좋다.
1. **변경한 slice** — detail body, used menu strip, price history, modal wrapper 등
2. **비교 후 유지한 slice** — supplier modal 등
3. **조건부로 미룬 slice** — shared core-ui 또는 navigation/state widening

## 구현 머지 후 동기화가 필요한 문서
공용 문서(`docs/MODULES.md`, `docs/REQUIREMENTS.md`)는 여러 작업자와 충돌하기 쉬우므로, 구현 머지 직후 아래 항목만 최소 갱신하는 방식을 권장한다.

1. `docs/MODULES.md`
   - `feature-ingredient`의 `IngredientDetailScreen` 설명을 “가격 이력 + 사용 메뉴” 수준에서 끝내지 말고, **가격 수정/공급업체 수정/refresh ownership**까지 반영한다.
   - `UsedMenuChip` 중심 설명이 남아 있으면 실제 컴포넌트 구조(`UsedMenuCard` 또는 그 후속 strip component)에 맞게 정리한다.
2. `docs/REQUIREMENTS.md`
   - 재료 상세 구현 현황에서 FR-004-008 / FR-004-009 상태를 실제 구현 결과와 맞춘다.
   - “사용 메뉴 칩 목록”처럼 Figma parity 이전 표현이 남아 있으면 strip/card presentation 기준으로 갱신한다.
3. 최종 QA 증빙
   - modal compare-first 결과(`223:3509`, `223:3609`, `223:3626`)를 release note나 PR 본문에도 재기록하면 재검토 비용을 줄일 수 있다.

## 이번 리뷰의 결론
- 가장 큰 구조 mismatch는 `FlowRow` 기반 사용 메뉴 섹션이다.
- 가장 큰 ownership mismatch 후보는 `IngredientEditBottomSheet` 안의 supplier field다.
- 반대로 뒤로가기 refresh 경로와 supplier 전용 modal entry는 이미 재사용 가치가 높아서, 특별한 parity blocker가 없다면 유지하는 편이 안전하다.
