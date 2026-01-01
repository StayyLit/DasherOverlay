#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

# Read distribution URL from wrapper properties
PROP="gradle/wrapper/gradle-wrapper.properties"
if [[ ! -f "$PROP" ]]; then
  echo "❌ Missing $PROP"
  exit 1
fi

DIST_URL=$(grep -E '^distributionUrl=' "$PROP" | cut -d= -f2- | sed 's/\\:/:/g')
ZIP_NAME="${DIST_URL##*/}"

mkdir -p .tmp gradle/wrapper
cd .tmp

echo "Downloading: $DIST_URL"
curl -L -o "$ZIP_NAME" "$DIST_URL"

echo "Finding wrapper jar inside distribution..."
JAR_PATH=$(unzip -Z1 "$ZIP_NAME" | grep -E 'gradle-wrapper(-shared)?-[0-9.]+\.jar$' | head -n1 || true)

if [[ -z "$JAR_PATH" ]]; then
  echo "❌ Could not find gradle-wrapper jar in distribution zip."
  echo "Here are wrapper-related entries:"
  unzip -Z1 "$ZIP_NAME" | grep -i wrapper | head -n 50 || true
  exit 1
fi

echo "Extracting: $JAR_PATH -> ../gradle/wrapper/gradle-wrapper.jar"
unzip -p "$ZIP_NAME" "$JAR_PATH" > ../gradle/wrapper/gradle-wrapper.jar

cd ..
rm -rf .tmp

echo "✅ gradle/wrapper/gradle-wrapper.jar created"
