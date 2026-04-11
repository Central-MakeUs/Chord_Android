#!/usr/bin/env bash
set -u

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

run_rg_check() {
  local title="$1"
  local expectation="$2"
  local pattern="$3"
  shift 3

  echo "== $title =="
  echo "Expectation: $expectation"
  echo "Command: rg -n \"$pattern\" $*"

  local output
  if output=$(rg -n --color=never "$pattern" "$@" 2>&1); then
    echo "$output"
  else
    local status=$?
    if [ "$status" -eq 1 ]; then
      echo "(no matches)"
    else
      echo "$output"
      return "$status"
    fi
  fi

  echo
}

print_review_note() {
  local title="$1"

  case "$title" in
    "Official SDK entry points only")
      echo "Note: the pre-integration baseline can legitimately show '(no matches)'; rerun after the Kakao/Naver wiring lands."
      ;;
    "No app-authored WebView/browser OAuth flow")
      echo "Note: current baseline hits in ChordNavHost can come from the unrelated settings WebView route; review new auth-file hits separately."
      ;;
    "No AuthRepository.signIn reachable from social-login flow")
      echo "Note: current baseline still exposes the credential login path; after the phase-1 migration, login-package hits must disappear."
      ;;
    "No stale success-navigation observer")
      echo "Note: current baseline still has legacy success-navigation state; the social-login probe screen should remove these hits."
      ;;
  esac

  echo
}

run_rg_check \
  "Official SDK entry points only" \
  "After integration, expect Kakao/Naver official SDK symbols only in app/bootstrap + feature-auth launcher code." \
  "UserApiClient|loginWithKakaoTalk|loginWithKakaoAccount|NidOAuth|requestLogin" \
  app/src/main feature/feature-auth/src/main
print_review_note "Official SDK entry points only"

run_rg_check \
  "No app-authored WebView/browser OAuth flow" \
  "Review any hit carefully; social-login implementation must not add its own WebView/browser OAuth flow." \
  "WebView|CustomTabsIntent|ACTION_VIEW|androidx.browser|oauth" \
  app/src/main feature/feature-auth/src/main
print_review_note "No app-authored WebView/browser OAuth flow"

run_rg_check \
  "No AuthRepository.signIn reachable from social-login flow" \
  "The login package should not retain provider-action paths that call AuthRepository.signIn(...)." \
  "AuthRepository|signIn\\(" \
  feature/feature-auth/src/main/kotlin/com/team/chord/feature/auth/login app/src/main
print_review_note "No AuthRepository.signIn reachable from social-login flow"

run_rg_check \
  "No stale success-navigation observer" \
  "The social-login probe screen should not retain isLoginSuccess-driven navigation side effects." \
  "isLoginSuccess|consumeLoginSuccess|onLoginSuccess|LaunchedEffect" \
  feature/feature-auth/src/main/kotlin/com/team/chord/feature/auth/login
print_review_note "No stale success-navigation observer"
