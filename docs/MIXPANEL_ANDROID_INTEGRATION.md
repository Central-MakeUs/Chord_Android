# Android Mixpanel Integration

## Overview

Android uses the official Mixpanel Android SDK through a dedicated `core:core-analytics` module. Feature modules should call the `Analytics` facade only; direct `MixpanelAPI` usage is confined to `MixpanelAnalyticsClient`.

- SDK artifact: `com.mixpanel.android:mixpanel-android`
- Current version: `8.8.0` from Maven Central metadata
- Initialization: `MainApplication` calls `Analytics.configure(this)` and tracks `app_launched`
- Automatic events: disabled via `MixpanelAPI.getInstance(context, token, false)`
- Missing token or disabled config: no-op analytics client

References:
- Official Android SDK docs: https://docs.mixpanel.com/docs/tracking-methods/sdks/android
- Official SDK source: https://github.com/mixpanel/mixpanel-android

## Local configuration

Add values to `local.properties`; never commit real tokens.

```properties
MIXPANEL_PROJECT_TOKEN=
MIXPANEL_ENABLED=true
MIXPANEL_LOGGING_ENABLED=false
```

`local.properties.example` contains empty defaults for local setup. If `MIXPANEL_PROJECT_TOKEN` is blank, tracking is disabled safely.

## Event taxonomy

Current Android events mirror the iOS low-risk taxonomy:

| Event | Properties |
| --- | --- |
| `app_launched` | `platform=android` |
| `login_screen_viewed` | `platform=android` |
| `social_login_tapped` | `platform=android`, `provider=kakao/naver` |
| `login_succeeded` | `platform=android`, `provider=kakao/naver` |
| `login_failed` | `platform=android`, `provider=kakao/naver`, `error_category=<allowlist>` |
| `onboarding_started` | `platform=android` |
| `onboarding_completed` | `platform=android` |
| `menu_registration_started` | `platform=android` |
| `menu_registration_completed` | `platform=android` |
| `logout_completed` | `platform=android` |
| `withdrawal_requested` | `platform=android` |
| `withdrawal_completed` | `platform=android` |

Allowed login failure categories are defined by `AnalyticsErrorCategory`, not free-form strings.

## Privacy guardrails

Do not send any of these to Mixpanel:

- access token / refresh token
- authorization code
- password
- email
- phone number
- user name / store name / menu name
- client secret

The current event properties intentionally use only platform, provider, and coarse error category. Logout and withdrawal call `flush()` and `reset()` after tracking completion so device-local Mixpanel identity state is cleared.

## Verification

Use Java 17:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH="$JAVA_HOME/bin:$PATH"
```

Recommended checks:

```bash
./gradlew :core:core-analytics:testDebugUnitTest --no-daemon --rerun-tasks
./gradlew :feature:feature-auth:testDebugUnitTest :core:core-network:testDebugUnitTest --no-daemon --rerun-tasks
./gradlew :app:assembleDebug --no-daemon
```

Static checks before PR:

- `MixpanelAPI` import exists only in `core/core-analytics`
- analytics call/property lines contain no token/PII keys
- `local.properties` remains untracked
