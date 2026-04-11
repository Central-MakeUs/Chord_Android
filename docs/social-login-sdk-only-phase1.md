# Social Login SDK Only (Phase 1)

## 목적
- `LOGIN_ROUTE`를 임시 **소셜 로그인 프로브 화면**으로 전환한다.
- Kakao / Naver 공식 Android SDK 로그인 진입만 제공하고, 성공 시 provider token 필드만 Logcat에 남긴다.
- 백엔드 토큰 교환, Chord 세션 생성, 로그인 성공 후 화면 이동은 모두 phase 1 범위 밖이다.

## 범위 / 비범위
### 포함
- Kakao / Naver 버튼만 노출하는 로그인 화면
- `MainApplication` 초기화 및 manifest / Gradle 설정 연결
- provider 결과를 화면 계층에서 받아 transient 상태와 로깅만 처리하는 검증
- SDK 미설정 환경에서도 수행 가능한 fallback proof path

### 제외
- Apple 로그인
- `AuthRepository.signIn(...)` 연동
- Chord auth/session persistence
- post-login navigation
- 앱이 직접 만든 WebView / browser OAuth 플로우

## 코드 리뷰 포인트
리뷰 시 아래 파일군이 계획과 일치하는지 우선 확인한다.

### `:app`
- `app/build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/team/chord/MainApplication.kt`

검토 기준:
- Kakao / Naver SDK 초기화에 필요한 의존성과 manifest wiring 이 존재하는가
- provider config 가 `local.properties` → Gradle/app config 경로로 주입되는가
- source code 에 secret 이 하드코딩되지 않았는가

### `:feature:feature-auth`
- `feature/feature-auth/build.gradle.kts`
- `feature/feature-auth/src/main/kotlin/com/team/chord/feature/auth/login/LoginScreen.kt`
- `feature/feature-auth/src/main/kotlin/com/team/chord/feature/auth/login/LoginUiState.kt`
- `feature/feature-auth/src/main/kotlin/com/team/chord/feature/auth/login/LoginViewModel.kt`
- `feature/feature-auth/src/main/kotlin/com/team/chord/feature/auth/login/SocialLoginLauncher.kt` 또는 동등 helper

검토 기준:
- ID / password 입력, 기존 로그인 CTA, signup CTA, Apple CTA 가 제거되었는가
- Kakao / Naver 버튼만 남았는가
- provider action 이 `AuthRepository.signIn(...)` 을 호출하지 않는가
- provider 성공 후에도 `LOGIN_ROUTE` 에 머무르며 stale success-navigation effect 가 없는가
- official provider SDK entry point 만 사용하고 app-authored OAuth flow 가 추가되지 않았는가

## 수동 검증 게이트
아래 값이 준비되지 않으면 full manual provider-success verification 을 완료로 주장하면 안 된다.

- Kakao native app key
- Naver client secret
- Naver client name
- Naver callback URL / scheme
- provider console registration / Android key hash

게이트가 닫혀 있으면 아래 fallback proof path 결과를 PR / 작업 보고에 포함한다.

## Fallback Proof Path
### 1) Targeted Gradle proof
```bash
./gradlew :feature:feature-auth:testDebugUnitTest
./gradlew :feature:feature-auth:compileDebugKotlin :app:compileDebugKotlin
./gradlew :app:assembleDebug
```

### 2) Static proof helper
```bash
./scripts/social-login-phase1-static-checks.sh
```

이 스크립트는 승인된 test spec 의 read-only `rg` 점검 4개를 그대로 모아 보여준다.

## 해석 규칙
- `UserApiClient`, `loginWithKakaoTalk`, `loginWithKakaoAccount`, `NidOAuth`, `requestLogin` 외의 앱 작성 OAuth 진입 코드는 추가되면 안 된다.
- `WebView`, `CustomTabsIntent`, `ACTION_VIEW`, `androidx.browser`, `oauth` 관련 hit 는 social-login 변경 파일에서 특히 엄격하게 검토한다.
- 로그인 패키지에서 `AuthRepository` / `signIn(` hit 가 남아 있으면 phase-1 gate 위반으로 본다.
- `isLoginSuccess`, `consumeLoginSuccess`, `onLoginSuccess`, `LaunchedEffect` 기반 성공 후 이동 로직은 social-login probe 화면에서 제거되어야 한다.
- 공식 SDK 내부에서 provider-managed browser / custom-tab fallback 이 보이더라도 앱이 직접 구현한 것이 아니면 phase 1 에서 허용한다.

## 보고 템플릿
작업 완료 또는 리뷰 보고 시 아래 형식을 권장한다.

```text
Verification:
- PASS/FAIL: :feature:feature-auth:testDebugUnitTest
- PASS/FAIL: :feature:feature-auth:compileDebugKotlin :app:compileDebugKotlin
- PASS/FAIL: :app:assembleDebug
- PASS/FAIL: ./scripts/social-login-phase1-static-checks.sh
- Manual gate: open/blocked (missing: ...)
```
