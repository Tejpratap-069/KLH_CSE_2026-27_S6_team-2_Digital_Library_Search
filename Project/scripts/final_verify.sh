#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
if [ ! -s data/resources.txt ]; then ./scripts/generate.sh 50000; fi
./scripts/test.sh
rm -rf build/classes
./scripts/compile.sh
python3 scripts/check_frontend.py
python3 scripts/check_reference_ui.py
python3 scripts/check_project.py
python3 scripts/validate_dataset.py
echo 'FINAL VERIFICATION PASSED'
