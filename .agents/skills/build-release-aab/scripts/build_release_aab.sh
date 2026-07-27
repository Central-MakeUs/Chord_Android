#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'EOF'
Usage: build_release_aab.sh [--project-root PATH]

Build the signed Android release App Bundle (.aab) for this repository.

Options:
  --project-root PATH   Repository root. Defaults to the current working directory.
  -h, --help            Show this help text.
EOF
}

fail() {
  printf 'Error: %s\n' "$1" >&2
  exit 1
}

java_major_version() {
  local java_path="$1"
  local version
  local major

  version="$("$java_path" -version 2>&1 | awk -F '"' '/version/ { print $2; exit }')"
  [[ -n "$version" ]] || return 1

  major="${version%%.*}"
  if [[ "$major" == "1" ]]; then
    version="${version#*.}"
    major="${version%%.*}"
  fi

  [[ "$major" =~ ^[0-9]+$ ]] || return 1
  printf '%s\n' "$major"
}

is_compatible_java() {
  local java_path="$1"
  local major

  [[ -x "$java_path" ]] || return 1
  major="$(java_major_version "$java_path")" || return 1
  (( major >= 17 ))
}

read_property() {
  local properties_file="$1"
  local key="$2"

  awk -v key="$key" '
    /^[[:space:]]*[#!]/ { next }
    {
      line = $0
      sub(/\r$/, "", line)
      if (match(line, /[=:]/) == 0) {
        next
      }

      property_key = substr(line, 1, RSTART - 1)
      property_value = substr(line, RSTART + 1)

      gsub(/^[[:space:]]+|[[:space:]]+$/, "", property_key)
      gsub(/^[[:space:]]+|[[:space:]]+$/, "", property_value)

      if (property_key == key) {
        print property_value
        exit
      }
    }
  ' "$properties_file"
}

project_root="$(pwd)"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --project-root)
      [[ $# -ge 2 ]] || fail "Missing value for --project-root."
      project_root="$2"
      shift 2
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      fail "Unknown argument: $1"
      ;;
  esac
done

[[ -d "$project_root" ]] || fail "Project root does not exist: $project_root"

project_root="$(cd "$project_root" && pwd)"
gradlew_path="${project_root}/gradlew"
gradle_file="${project_root}/app/build.gradle.kts"
local_properties="${project_root}/local.properties"
artifact_path="${project_root}/app/build/outputs/bundle/release/app-release.aab"

[[ -f "$gradle_file" ]] || fail "Expected Gradle config at ${gradle_file}"
[[ -f "$local_properties" ]] || fail "Missing local.properties at ${local_properties}"
[[ -x "$gradlew_path" ]] || fail "Gradle wrapper is not executable: ${gradlew_path}"

required_keys=(
  "RELEASE_STORE_FILE"
  "RELEASE_STORE_PASSWORD"
  "RELEASE_KEY_ALIAS"
  "RELEASE_KEY_PASSWORD"
  "KAKAO_NATIVE_APP_KEY"
  "NAVER_CLIENT_ID"
  "NAVER_CLIENT_SECRET"
  "NAVER_CLIENT_NAME"
)

missing_keys=()
release_store_file=""

for key in "${required_keys[@]}"; do
  value="$(read_property "$local_properties" "$key")"
  if [[ -z "$value" ]]; then
    missing_keys+=("$key")
    continue
  fi

  if [[ "$key" == "RELEASE_STORE_FILE" ]]; then
    release_store_file="$value"
  fi
done

if (( ${#missing_keys[@]} > 0 )); then
  fail "Release properties are missing in local.properties: ${missing_keys[*]}"
fi

if [[ "$release_store_file" != /* ]]; then
  release_store_file="${project_root}/${release_store_file}"
fi

[[ -f "$release_store_file" ]] || fail "Release keystore file not found: ${release_store_file}"

android_studio_jbr="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
path_java="$(command -v java 2>/dev/null || true)"

if [[ -n "${JAVA_HOME:-}" ]] && is_compatible_java "${JAVA_HOME}/bin/java"; then
  :
elif [[ -n "$path_java" ]] && is_compatible_java "$path_java"; then
  unset JAVA_HOME
elif is_compatible_java "${android_studio_jbr}/bin/java"; then
  export JAVA_HOME="${android_studio_jbr}"
  export PATH="${JAVA_HOME}/bin:${PATH}"
else
  fail "No compatible Java runtime found. Install Java 17+ or Android Studio."
fi

(
  cd "$project_root"
  "$gradlew_path" :app:bundleRelease
)

[[ -f "$artifact_path" ]] || fail "Release AAB was not produced at ${artifact_path}"

artifact_size_bytes="$(wc -c < "$artifact_path" | tr -d '[:space:]')"
artifact_size_human="$(du -h "$artifact_path" | awk '{print $1}')"

printf 'AAB_PATH=%s\n' "$artifact_path"
printf 'AAB_SIZE_BYTES=%s\n' "$artifact_size_bytes"
printf 'AAB_SIZE_HUMAN=%s\n' "$artifact_size_human"
