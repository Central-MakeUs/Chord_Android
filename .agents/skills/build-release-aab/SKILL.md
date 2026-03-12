---
name: build-release-aab
description: Build the signed Android release App Bundle (.aab) for this repository. Use when the user asks to create or export a release AAB, run `bundleRelease`, bump the Android app version for release, verify release signing configuration, or report the final bundle output path and size.
---

# Build Release AAB

Build the signed `release` Android App Bundle for this project and return the final artifact path.

## Workflow

### 1. Check version only when needed

- Read `app/build.gradle.kts` to inspect `versionName` and `versionCode`.
- If the user asks for a version change, edit `app/build.gradle.kts` with `apply_patch`.
- Keep `versionCode` monotonically increasing. Never reset it downward just because `versionName` changed.
- Do not change the version unless the user explicitly asks or the release task clearly requires it.

### 2. Verify release signing inputs

- Confirm `local.properties` exists in the repo root.
- Confirm these keys are present and non-empty:
  - `RELEASE_STORE_FILE`
  - `RELEASE_STORE_PASSWORD`
  - `RELEASE_KEY_ALIAS`
  - `RELEASE_KEY_PASSWORD`
- Stop and report the missing keys instead of attempting a release build if any are absent.

### 3. Run the release bundle build

- From the repo root, run:

```bash
./.agents/skills/build-release-aab/scripts/build_release_aab.sh --project-root "$PWD"
```

- The script will:
  - detect a working Java runtime, preferring Android Studio's bundled JBR when needed
  - verify signing properties before building
  - run `./gradlew :app:bundleRelease`
  - print machine-readable output lines for the artifact path and size

### 4. Report the result

- Return the final `versionName` and `versionCode` if they changed.
- Return the absolute AAB path.
- Mention the expected output location:
  - `app/build/outputs/bundle/release/app-release.aab`
- Mention the file size if the build succeeded.

## Guardrails

- Do not commit, tag, upload, or publish unless the user explicitly asks.
- Do not modify `local.properties` or keystore files.
- Do not revert unrelated tracked changes in the working tree.
- Treat `.idea/`, `.gradle/`, and `.kotlin/` as build/IDE artifacts, not deliverables.

## Notes

- This repository already enforces release signing for `bundleRelease` in `app/build.gradle.kts`.
- If `java` is unavailable in the shell, prefer `/Applications/Android Studio.app/Contents/jbr/Contents/Home`.
- If the script succeeds, the bundle should be at `app/build/outputs/bundle/release/app-release.aab`.
