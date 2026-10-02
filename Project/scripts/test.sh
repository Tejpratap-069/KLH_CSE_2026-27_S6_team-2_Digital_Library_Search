#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
rm -rf build/classes && mkdir -p build/classes
find backend/src backend/test -name '*.java' | sort > build/test-sources.txt
javac -encoding UTF-8 -d build/classes @build/test-sources.txt
java -ea -cp build/classes digitallibrary.AllTests
python3 scripts/check_frontend.py
python3 scripts/check_project.py
python3 scripts/check_windows_batch_paths.py
python3 scripts/validate_dataset.py
if command -v node >/dev/null 2>&1; then for f in frontend/js/*.js; do node --check "$f"; done; fi
