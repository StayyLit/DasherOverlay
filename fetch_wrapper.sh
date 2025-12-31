#!/usr/bin/env bash
set -euo pipefail
mkdir -p gradle/wrapper
JAR_URL="https://raw.githubusercontent.com/gradle/gradle/v8.10.2.0/gradle/wrapper/gradle-wrapper.jar"
# If that URL fails (version mismatch), fall back to master wrapper jar (works with properties dist)
FALLBACK_URL="https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar"

echo "Downloading gradle-wrapper.jar..."
if command -v curl >/dev/null 2>&1; then
  curl -L "$JAR_URL" -o gradle/wrapper/gradle-wrapper.jar || curl -L "$FALLBACK_URL" -o gradle/wrapper/gradle-wrapper.jar
elif command -v wget >/dev/null 2>&1; then
  wget -O gradle/wrapper/gradle-wrapper.jar "$JAR_URL" || wget -O gradle/wrapper/gradle-wrapper.jar "$FALLBACK_URL"
else
  echo "Need curl or wget." >&2
  exit 1
fi
echo "OK."
