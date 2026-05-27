# Project Documentation

## Root
- `local.properties.example` - 로컬/SDK 키 설정 템플릿 (`local.properties`는 커밋 금지)

## Current Auth / Social Login Contract
- Android 소셜 로그인은 Kakao/Naver SDK에서 provider access token을 받은 뒤 서버 모바일 endpoint에 전달한다.
  - Kakao: `POST /api/v1/auth/kakao/login`
  - Naver: `POST /api/v1/auth/naver/login`
- Android에는 Apple 로그인을 구현하지 않는다. Apple 로그인은 iOS 전용이다.
- 2026-05-26 기준 서버에는 Google 로그인 endpoint/DTO가 없다. Android에서 Google 로그인은 서버 계약 전까지 제외한다.
- 소셜 탈퇴는 legacy `DELETE /api/v1/users/me`가 아니라 `POST /api/v1/users/me`를 사용한다.
  - Request body: `{ "accessToken": "<provider-access-token-or-null>" }`
  - Kakao는 backend 저장 subject 기반 unlink라 token은 선택값이다.
  - Naver는 provider access token이 필요하므로 SDK 로그인 성공 시 저장한 token을 전달한다.
- SDK 키는 `local.properties`로 주입한다.
  - `KAKAO_NATIVE_APP_KEY`
  - `NAVER_CLIENT_ID`
  - `NAVER_CLIENT_SECRET`
  - `NAVER_CLIENT_NAME`
- Kakao Android key hash는 Kakao Developers 콘솔에 등록되어 있어야 SDK 로그인이 성공한다.

## AI Assistant Setup (OpenAI Codex)

이 저장소는 Codex의 `AGENTS.md` + `.agents/skills/` 구조를 기준으로 운영합니다.

### Prompt Packs
- `docs/prompt-packs/senior-android-engineer.md` - Android/Kotlin implementation and review guide
- `docs/prompt-packs/requirements-analyzer.md` - Requirements discovery and documentation guide

### Project Docs
- `docs/ARCHITECTURE.md` - Architecture decisions and patterns
- `docs/CONVENTIONS.md` - Code conventions and style guide
- `docs/MODULES.md` - Module structure and dependencies
- `docs/REQUIREMENTS.md` - Product requirements

### Skills
- `.agents/skills/build-release-aab/SKILL.md` - Release AAB build workflow guide
- `.agents/skills/commit/SKILL.md` - Commit workflow guide
- `.agents/skills/sync-docs/SKILL.md` - Documentation sync workflow guide
