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
  fail "Release signing properties are missing in local.properties: ${missing_keys[*]}"
fi

if [[ "$release_store_file" != /* ]]; then
  release_store_file="${project_root}/${release_store_file}"
fi

[[ -f "$release_store_file" ]] || fail "Release keystore file not found: ${release_store_file}"

android_studio_jbr="/Applications/Android Studio.app/Contents/jbr/Contents/Home"

if [[ -n "${JAVA_HOME:-}" ]] && [[ -x "${JAVA_HOME}/bin/java" ]] && "${JAVA_HOME}/bin/java" -version >/dev/null 2>&1; then
  :
elif command -v java >/dev/null 2>&1 && java -version >/dev/null 2>&1; then
  :
elif [[ -x "${android_studio_jbr}/bin/java" ]] && "${android_studio_jbr}/bin/java" -version >/dev/null 2>&1; then
  export JAVA_HOME="${android_studio_jbr}"
  export PATH="${JAVA_HOME}/bin:${PATH}"
else
  fail "No working Java runtime found. Install Java or Android Studio."
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
