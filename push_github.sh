#!/data/data/com.termux/files/usr/bin/bash
set -e

echo "=== GitHub Push Helper (Termux) ==="

# Ensure we're in a git repo
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "ERROR: Not inside a git repository."
  echo "Run: cd ~/DasherOverlay"
  exit 1
fi

# Ask for repo info
read -p "GitHub username (example: StayyLit): " GH_USER
read -p "GitHub repo name (example: DasherOverlay): " GH_REPO

if [ -z "$GH_USER" ] || [ -z "$GH_REPO" ]; then
  echo "ERROR: Username and repo name are required."
  exit 1
fi

REMOTE_URL="https://github.com/${GH_USER}/${GH_REPO}.git"
echo "Using remote: $REMOTE_URL"

# Fix or set origin
if git remote | grep -q origin; then
  git remote set-url origin "$REMOTE_URL"
else
  git remote add origin "$REMOTE_URL"
fi

# Show status
echo
git status
echo

# Commit everything
read -p "Commit message (leave empty for 'update'): " MSG
MSG=${MSG:-update}

git add .
git commit -m "$MSG" || echo "Nothing new to commit."

echo
echo ">>> PUSHING TO GITHUB <<<"
echo "When prompted:"
echo "  Username = your GitHub username"
echo "  Password = PASTE YOUR PERSONAL ACCESS TOKEN"
echo

git push -u origin main

echo
echo "✅ Push complete!"
