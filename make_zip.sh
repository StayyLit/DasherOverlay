#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
ZIP="DasherOverlay.zip"
rm -f "$ZIP"
# exclude gradle caches
zip -r "$ZIP" . -x "*.gradle" -x "**/.gradle/**" -x "**/build/**" -x "**/.idea/**"
echo "Created $ZIP"
